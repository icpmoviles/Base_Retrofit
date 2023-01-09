package es.icp.base_retrofit.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import es.icp.base_retrofit.application.OfflineApplication
import es.icp.base_retrofit.communication.RetrofitBase
import es.icp.base_retrofit.communication.RetrofitBase.mGson
import es.icp.base_retrofit.database.OfflineService
import es.icp.base_retrofit.models.NetworkResponse
import es.icp.base_retrofit.models.ResponseState
import es.icp.base_retrofit.models.UiState
import kotlinx.coroutines.*
import retrofit2.create

const val TAG = "RETROFIT"
typealias GenericResponse<S> = NetworkResponse<S, Error>

/**
 * FUNCION PARA PROCESAR EL RESULTADO DE LA LLAMADA. SI AMBOS PARAMETROS SON NULL ACTUA COMO UNA LLAMADA BASICA
 * @param context Contexto de la aplicacion, si no es null y soporteOffline esta activado persiste la accion fallida,
 * cuando soporteOffline esta desactivado pero se manda context se enviaran acciones pendientes si las hubiera
 * @param soperteOffline activa la persistencia de la acciones, si context es null no persistira los datos, solo
 * devolvera el estado offline
 */
suspend fun NetworkResponse<Any, Error>.executeCall(context: Context? = null, soperteOffline: Boolean = false, guardarAccion: Boolean = false) : ResponseState {
   return when (val response = this) {
       is NetworkResponse.Success -> {
           Log.w("$TAG ${this.javaClass.simpleName}", mGson.toJson(response.body))
           ResponseState.Ok( response.body)
       }
       is NetworkResponse.HttpError ->  {
           Log.w("$TAG ${this.javaClass.simpleName}", "${response.code} ${response.message}")
           ResponseState.Error(
               code = response.code,
               message = response.message
           )
       }
       is NetworkResponse.NetworkError -> {
           if (soperteOffline) {
               context?.let {
                   if (guardarAccion) (it as OfflineApplication).repoAccion.insertAccion(response.accionOffline)
               }
               return ResponseState.Offline
           }
           Log.w("$TAG ${this.javaClass.simpleName}", "${response.code} ${response.error.message}")
           ResponseState.Error(
               code = response.code ?: -1,
               message = response.error.message?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
       }
       is NetworkResponse.UnknownError -> {
           Log.w("$TAG ${this.javaClass.simpleName}", response.error?.message ?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
           ResponseState.Error(
               code = -1,
               message = response.error?.message ?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos."
           )
       }
   }
}

suspend fun checkAccionesPendientes(context: Context) {
    CoroutineScope(Dispatchers.IO)
        .launch {
            (context as OfflineApplication).repoAccion.getAllAcciones()?.let { lista ->
                if (lista.isNotEmpty()) {
                    lista.forEach { accion->
                        delay(1000)
                        if (accion.url.isNotEmpty() && accion.metodo == "POST"){
                            val url = accion.url.replace(accion.url.split("/").last(),"", true)
                            val retrofit = RetrofitBase.getInstance(url)
                            val service = retrofit.create<OfflineService>()
                            service.sendOfflineAction(
                                endpoint = accion.url.split("/").last(),
                                accionOffline = JsonParser().parse(accion.json).asJsonObject
                            ).executeCall().let {
                                if (it is ResponseState.Ok) {
                                    context.repoAccion.deleteByAccion(accion)
                                    Log.w("ACCION ELIMINADA ", accion.toString())
                                }
                            }
                        }
                    }
                }
            }
        }.join()
}
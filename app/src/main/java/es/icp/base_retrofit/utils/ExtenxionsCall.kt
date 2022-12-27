package es.icp.base_retrofit.utils

import android.util.Log
import es.icp.base_retrofit.communication.NetworkResponse
import es.icp.base_retrofit.communication.UiState

const val TAG = "RETROFIT"
typealias GenericResponse<S> = NetworkResponse<S, Error>

fun NetworkResponse<Any, Error>.executeCall(isSuccessfull: (Boolean?) -> Unit , errorResult: (Int,String) -> Unit?) : Any? {
   return when (val response = this) {
       is NetworkResponse.Success -> {
           Log.w("$TAG SUCCES", response.body.toString())
           isSuccessfull.invoke(true)
           response.body
       }
       is NetworkResponse.HttpError ->  {
           errorResult.invoke(response.code, response.message)
           Log.w("$TAG HTTP_ERROR", "${response.code} ${response.message}")
           null
       }
       is NetworkResponse.NetworkError -> {
           errorResult.invoke(response.code ?: -1, response.error.message?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos." )
           Log.w("$TAG NETWORK_ERROR", "${response.code} ${response.error.message}")
           null
       }
       is NetworkResponse.UnknownError -> {
           errorResult.invoke(-1, response.error?.message ?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
           Log.w("$TAG UNKNOWN_ERROR", response.error?.message ?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
           null
       }
   }
}
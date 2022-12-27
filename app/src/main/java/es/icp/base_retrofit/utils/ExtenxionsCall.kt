package es.icp.base_retrofit.utils

import android.util.Log
import es.icp.base_retrofit.communication.NetworkResponse
import es.icp.base_retrofit.communication.UiState
import kotlinx.coroutines.flow.MutableStateFlow

const val TAG = "RETROFIT"
typealias GenericResponse<S> = NetworkResponse<S, Error>

fun NetworkResponse<Any, Error>.executeCall(state: MutableStateFlow<UiState>) : Any? {
   return when (val response = this) {
       is NetworkResponse.Success -> {
           Log.w("$TAG SUCCES", response.body.toString())
           state.value = UiState.Success
           response.body
       }
       is NetworkResponse.HttpError ->  {
           state.value = UiState.Error(response.code, response.message)
           Log.w("$TAG HTTP_ERROR", "${response.code} ${response.message}")
           null
       }
       is NetworkResponse.NetworkError -> {
           state.value = UiState.Error(response.code ?: -1, response.error.message?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
           Log.w("$TAG NETWORK_ERROR", "${response.code} ${response.error.message}")
           null
       }
       is NetworkResponse.UnknownError -> {
           state.value = UiState.Error( -1, response.error?.message ?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
           Log.w("$TAG UNKNOWN_ERROR", response.error?.message ?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
           null
       }
   }
}
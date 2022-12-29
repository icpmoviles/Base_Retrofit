package es.icp.base_retrofit.utils

import android.util.Log
import es.icp.base_retrofit.communication.BaseApiResponse
import es.icp.base_retrofit.communication.NetworkResponse
import es.icp.base_retrofit.communication.UiState
import kotlinx.coroutines.flow.MutableStateFlow

const val TAG = "RETROFIT"
typealias GenericResponse<S> = NetworkResponse<S, Error>

fun NetworkResponse<Any, Error>.executeCall() : UiState {
   return when (val response = this) {
       is NetworkResponse.Success -> {
           Log.w("$TAG SUCCES", response.body.toString())
           UiState.Succes(response.body as BaseApiResponse<*>)
       }
       is NetworkResponse.HttpError ->  {
           Log.w("$TAG HTTP_ERROR", "${response.code} ${response.message}")
           UiState.Error(response.code, response.message)
       }
       is NetworkResponse.NetworkError -> {
           Log.w("$TAG NETWORK_ERROR", "${response.code} ${response.error.message}")
           UiState.Error(response.code ?: -1, response.error.message?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
       }
       is NetworkResponse.UnknownError -> {
           Log.w("$TAG UNKNOWN_ERROR", response.error?.message ?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
           UiState.Error( -1, response.error?.message ?: "Se ha producido un error desconocido. Por favor, vuelva a intentarlo pasados unos minutos.")
       }
   }
}
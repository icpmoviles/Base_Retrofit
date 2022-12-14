package es.icp.base_retrofit.utils

import androidx.lifecycle.MutableLiveData
import es.icp.base_retrofit.communication.NetworkResponse

typealias GenericResponse<S> = NetworkResponse<S, Error>

fun NetworkResponse<Any, Error>.executeCall(onError : MutableLiveData<String?>) : Any? {
   return when (val response = this) {
       is NetworkResponse.Success -> response.body
       is NetworkResponse.HttpError ->  {
           onError.postValue("${response.code} ${response.message}")
            null
       }
       is NetworkResponse.NetworkError -> {
           onError.postValue("${response.code} ${response.error}")
           null
       }
       is NetworkResponse.UnknownError -> {
           onError.postValue("${response.error}")
           null
       }
   }
}
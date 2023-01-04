package es.icp.base_retrofit.models

sealed class ResponseState {
    data class Ok(val data: Any?): ResponseState()
    data class Error (val code: Int, val message: String): ResponseState()
    object Offline: ResponseState()
}

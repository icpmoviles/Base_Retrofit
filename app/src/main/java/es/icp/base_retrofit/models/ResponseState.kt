package es.icp.base_retrofit.models

import es.icp.base_retrofit.communication.BaseApiResponse

sealed class ResponseState {
    data class Body(val baseApiResponse: BaseApiResponse<*>?): ResponseState()
    data class Error (val code: Int, val message: String): ResponseState()
}

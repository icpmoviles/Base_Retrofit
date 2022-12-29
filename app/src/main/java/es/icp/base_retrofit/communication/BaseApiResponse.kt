package es.icp.base_retrofit.communication

import java.io.Serializable

data class BaseApiResponse<T> (
    val retcode : Int? = null,
    val mensaje: String = "",
    val data: T? = null
): Serializable
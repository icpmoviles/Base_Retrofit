package es.icp.base_retrofit.communication

class BaseApiResponse<T> {
    val retcode : Int? = null
    val mensaje: String = ""
    val data: T? = null
}
package es.icp.base_retrofit.database

import com.google.gson.JsonObject
import es.icp.base_retrofit.models.AccionOffline
import es.icp.base_retrofit.utils.GenericResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface OfflineService {
    @POST
    suspend fun sendOfflineAction(
        @Url endpoint: String,
        @Body accionOffline: JsonObject
    ) : GenericResponse<Any>

}
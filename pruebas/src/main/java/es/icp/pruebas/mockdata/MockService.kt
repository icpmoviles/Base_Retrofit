package es.icp.pruebas.mockdata

import es.icp.base_retrofit.communication.BaseApiResponse
import es.icp.base_retrofit.utils.GenericResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface MockService {
    @POST
    suspend fun doLogin(
        @Url url: String ,
        @Body mockreques: MockModel
    ) : GenericResponse<MockModel>

}
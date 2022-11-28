package es.icp.base_retrofit.communication

import okhttp3.*

class RefreshAuthToken(
    private val otherHeaders: Headers? = null,
    private val refresToken: (String) -> Unit = { }
    ) : Authenticator {



    override fun authenticate(route: Route?, response: Response): Request? {
        var requestAvailable: Request? = null
        try {
            val updatedToken = refresToken.toString()

            requestAvailable = otherHeaders?.let {
                response.request().newBuilder()
                    .headers(otherHeaders)
                    .addHeader("Authorization", "Bearer $updatedToken")
                    .build()
            }?: kotlin.run {
                response.request().newBuilder()
                    .addHeader("Authorization", "Bearer $updatedToken")
                    .build()
            }

        } catch (ex: Exception) { ex.printStackTrace() }
        return requestAvailable
    }
}
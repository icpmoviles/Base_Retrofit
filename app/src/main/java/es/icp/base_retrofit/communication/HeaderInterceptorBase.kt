package es.icp.base_retrofit.communication

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptorBase (
    private val headers: Headers
        ) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .headers(headers)
            .build()
        return chain.proceed(request)
    }
}
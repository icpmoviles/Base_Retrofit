package es.icp.base_retrofit.communication

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBase {

    var retrofit: Retrofit? = null

    fun getInstance(
        baseUrl: String,
        client: OkHttpClient? = null
    ) : Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
                ))

        return retrofit ?: kotlin.run {
            retrofit = client?.let {
                builder.client(it).build()
            } ?: kotlin.run {
                builder.build()
            }
            retrofit!!
        }
    }
}
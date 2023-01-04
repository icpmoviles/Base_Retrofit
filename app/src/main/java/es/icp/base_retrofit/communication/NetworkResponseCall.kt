package es.icp.base_retrofit.communication

import android.util.Log
import com.google.gson.Gson
import es.icp.base_retrofit.models.AccionOffline
import es.icp.base_retrofit.models.NetworkResponse
import es.icp.base_retrofit.utils.HttpCodes.ACCEPTED
import es.icp.base_retrofit.utils.HttpCodes.NOT_CONTENT
import es.icp.base_retrofit.utils.HttpCodes.OK
import es.icp.base_retrofit.utils.TAG
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.util.*


internal class NetworkResponseCall<S: Any, E: Any> (
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
        ): Call<NetworkResponse<S, E>> {

    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()
                Log.w("$TAG CALL", response.toString())
                if (response.isSuccessful){
                    when (code) {
                         in OK..ACCEPTED -> {
                             body?.let {
                                     callback.onResponse(
                                     this@NetworkResponseCall,
                                     Response.success(NetworkResponse.Success(it))
                                 )
                            }
                        }
                        NOT_CONTENT -> {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(NetworkResponse.UnknownError(Error("El recurso solicitado no ha devuelto contenido.")))
                            )
                        }

                        else -> callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.HttpError(code, response.headers()["message"] ?: response.message() ))
                        )

                    }
                } else {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(NetworkResponse.HttpError(code,  response.headers()["message"] ?: response.message()))
                    )
                }

            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                Log.w("$TAG ONFAILURE", throwable.message.toString())
                throwable.printStackTrace()
                val networkResponse = when (throwable) {
                    is IOException ->
                        NetworkResponse.NetworkError(
                            error = throwable,
                            code = null,
                            accionOffline = AccionOffline(
                                id = null,
                                json = Gson().toJson(this@NetworkResponseCall.request().tag(Invocation::class.java)?.arguments()?.last()),
                                url = delegate.request().url().toString(),
                                metodo = delegate.request().method(),
                                f_insert = Date()
                            )
                        )
                    else -> NetworkResponse.UnknownError(throwable)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
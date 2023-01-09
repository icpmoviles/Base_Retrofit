package es.icp.pruebas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import es.icp.base_retrofit.application.OfflineApplication
import es.icp.base_retrofit.communication.RetrofitBase
import es.icp.base_retrofit.models.ResponseState
import es.icp.base_retrofit.utils.checkAccionesPendientes
import es.icp.base_retrofit.utils.executeCall
import es.icp.base_retrofit.utils.executeCallWithCheck
import es.icp.pruebas.mockdata.MockModel
import es.icp.pruebas.mockdata.MockService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.create
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RetrofitBase.getInstance(
            baseUrl = "https://63b54fad0f49ecf508a0abcb.mockapi.io/mock/"
        )
        val service = retrofit.create<MockService>()
        val repo = (this.application as OfflineApplication).repoAccion

        CoroutineScope(Dispatchers.IO).launch {
            val lista = repo.getAllAcciones()
            Log.w("LISTA", lista.toString())
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            val response = service.doLogin(
//                url = "prueba",
//                mockreques = MockModel(
//                    "5550",
//                    Date(),
//                    "4"
//                )
//            ).executeCallWithCheck(
//                    this@MainActivity.applicationContext,
//                    true,
//                    guardarAccion = true
//                )
//
//            when (response) {
//                is ResponseState.Error -> Log.w("main error", response.toString())
//                is ResponseState.Ok -> Log.w("main ok", response.data.toString())
//                is ResponseState.Offline -> Log.w("main ofline", response.toString())
//            }
//
//        }

        val job = CoroutineScope(Dispatchers.IO).launch {
            val response = service.doLogin(
                url = "prueba",
                mockreques = MockModel(
                    "5550",
                    Date(),
                    "2"
                )
            ).executeCallWithCheck(
                this@MainActivity.applicationContext,
                true,
                guardarAccion = true
            ) {

                when (it) {
                    is ResponseState.Error -> Log.w("main error", it.toString())
                    is ResponseState.Ok -> Log.w("main ok", it.data.toString())
                    is ResponseState.Offline -> Log.w("main ofline", it.toString())
                }
            }

        }


    }
}
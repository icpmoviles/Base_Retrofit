package es.icp.base_retrofit.application

import android.app.Application
import es.icp.base_retrofit.database.AccionesOfflineDB
import es.icp.base_retrofit.database.repo.RepoAccion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

open class OfflineApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    val databaseOffline by lazy { AccionesOfflineDB.getDatabase(this, applicationScope) }
    val repoAccion by lazy { RepoAccion(databaseOffline.accionesOfflineDao()) }


}
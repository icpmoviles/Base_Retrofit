package es.icp.base_retrofit.database.repo

import es.icp.base_retrofit.database.dao.AccionOfflineDAO
import es.icp.base_retrofit.models.AccionOffline

class RepoAccion constructor(
    private val accionOfflineDAO: AccionOfflineDAO
){

    suspend fun insertAccion(accionOffline: AccionOffline) = accionOfflineDAO.insertAccion(accionOffline)
    fun getAllAcciones() = accionOfflineDAO.getAllAcciones()
    fun getAccionById(id: Int) = accionOfflineDAO.getAccionById(id)
    fun deleteByList(list: List<AccionOffline>) = accionOfflineDAO.deleteByList(list)
    fun deleteByAccion(accionOffline: AccionOffline) = accionOfflineDAO.deleteByAccion(accionOffline)
    fun deleteAll() = accionOfflineDAO.deleteAll()
}
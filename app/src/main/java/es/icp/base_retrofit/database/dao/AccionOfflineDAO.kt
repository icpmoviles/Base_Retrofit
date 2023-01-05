package es.icp.base_retrofit.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.icp.base_retrofit.models.AccionOffline

@Dao
interface AccionOfflineDAO {

  @Query("SELECT * FROM ACCIONES_OFFLINE")
  fun getAllAcciones(): MutableList<AccionOffline>?

  @Query("SELECT * FROM ACCIONES_OFFLINE WHERE id = :id")
  fun getAccionById(id: Int): AccionOffline

  @Delete
  fun deleteByList(list: List<AccionOffline>)

  @Delete
  fun deleteByAccion(accionOffline: AccionOffline)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAccion(accionOffline: AccionOffline)

  @Query("DELETE FROM ACCIONES_OFFLINE")
  fun deleteAll()
}
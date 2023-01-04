package es.icp.base_retrofit.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "ACCIONES_OFFLINE")
data class AccionOffline(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,

    val json: String,

    val url: String,

    val metodo: String,

    val f_insert: Date = Date(),

)

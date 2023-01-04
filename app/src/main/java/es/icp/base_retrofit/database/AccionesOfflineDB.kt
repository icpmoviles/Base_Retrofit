package es.icp.base_retrofit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import es.icp.base_retrofit.database.converters.RoomTypeConverters
import es.icp.base_retrofit.database.dao.AccionOfflineDAO
import es.icp.base_retrofit.models.AccionOffline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        AccionOffline::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class AccionesOfflineDB : RoomDatabase(){

    abstract fun accionesOfflineDao() : AccionOfflineDAO


    private class OfflineDbCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {

                }
            }
        }

    }

    companion object {
        @Volatile
        private var INSTANCE: AccionesOfflineDB? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AccionesOfflineDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccionesOfflineDB::class.java,
                    "offline_ICP_Db"
                )
                    .addCallback(OfflineDbCallback(scope))
                    .addTypeConverter(RoomTypeConverters())
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

}


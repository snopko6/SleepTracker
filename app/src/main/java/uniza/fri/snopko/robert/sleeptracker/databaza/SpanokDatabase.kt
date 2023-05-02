package uniza.fri.snopko.robert.sleeptracker.databaza

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Spanok::class], version = 1, exportSchema = false)
abstract class SpanokDatabase : RoomDatabase() {
    abstract fun spanokDao(): SpanokDao

    companion object {
        @Volatile
        private var INSTANCE: SpanokDatabase? = null

        fun getDatabase(context: Context): SpanokDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpanokDatabase::class.java,
                    "zoznam_spankov"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

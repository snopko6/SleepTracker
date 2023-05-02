package uniza.fri.snopko.robert.sleeptracker.databaza

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Spanok::class], version = 1)
abstract class SpanokDatabase : RoomDatabase() {
    abstract fun databazaDao(): SpanokDao

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
                    "zaznam_spankov"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

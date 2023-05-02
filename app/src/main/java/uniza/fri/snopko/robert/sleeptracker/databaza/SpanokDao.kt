package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SpanokDao {
    @Insert
    suspend fun pridajSpanok(databaza: Spanok)
    @Update
    suspend fun update(databaza: Spanok)
    @Delete
    suspend fun delete(databaza: Spanok)
    @Query("SELECT * FROM zaznam_spankov ORDER BY id ASC")
    fun nacitajSpanky(): LiveData<List<Spanok>>
}
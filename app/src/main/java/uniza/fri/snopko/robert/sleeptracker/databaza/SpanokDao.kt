package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SpanokDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun pridajSpanok(spanok: Spanok)

    @Update
    suspend fun update(spanok: Spanok)

    @Delete
    suspend fun delete(spanok: Spanok)

    @Query("SELECT AVG(skore) FROM zoznam_spankov")
    fun dajPriemerneSkore(): LiveData<Int>

    @Query("SELECT * FROM zoznam_spankov ORDER BY id ASC")
    fun nacitajSpanky(): LiveData<List<Spanok>>

    @Query("DELETE FROM zoznam_spankov")
    suspend fun vymazVsetkySpanky()
}
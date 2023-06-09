package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SpanokDao {

    /**
     * Pridá spánok do databázy
     *
     * @param spanok Záznam o spánku
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun pridajSpanok(spanok: Spanok)

    /**
     * Získa priemerné skóre spánkov
     *
     * @return LiveData obsahujúce priemerné skóre
     */
    @Query("SELECT AVG(skore) FROM zoznam_spankov")
    fun dajPriemerneSkore(): LiveData<Int>

    /**
     * Získa priemerné skóre spánkov ako celé číslo
     *
     * @return Priemerné skóre spánkov
     */
    @Query("SELECT AVG(skore) FROM zoznam_spankov")
    fun dajPriemerneSkoreInt(): Int

    /**
     * Získa všetky spánky z databázy, zoradené vzostupne podľa ich ID
     *
     * @return LiveData obsahujúci zoradený zoznam spánkov
     */
    @Query("SELECT * FROM zoznam_spankov ORDER BY id ASC")
    fun nacitajSpanky(): LiveData<List<Spanok>>

    /**
     * Vymaže všetky spánky z databazy
     */
    @Query("DELETE FROM zoznam_spankov")
    suspend fun vymazVsetkySpanky()
}
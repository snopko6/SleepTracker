package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.lifecycle.LiveData

/**
 * Repozitár ktorý abstrahuje prístup k databáze
 *
 * @property spanokDao DAO databázy
 */
class SpanokRepository(private val spanokDao: SpanokDao) {

    val nacitajSpanky: LiveData<List<Spanok>> = spanokDao.nacitajSpanky()

    /**
     * Pridá spánok do databázy
     *
     * @param spanok Záznam o spánku
     */
    suspend fun pridajSpanok(spanok: Spanok) {
        spanokDao.pridajSpanok(spanok)
    }

    /**
     * Získa priemerné skóre spánkov
     *
     * @return LiveData obsahujúce priemerné skóre
     */
    fun dajPriemerneSkore(): LiveData<Int> {
        return spanokDao.dajPriemerneSkore()
    }

    /**
     * Získa priemerné skóre spánkov ako celé číslo
     *
     * @return Priemerné skóre spánkov
     */
    fun dajPriemerneSkoreInt(): Int {
        return spanokDao.dajPriemerneSkoreInt()
    }

    /**
     * Vymaže všetky spánky z databazy
     */
    suspend fun vymazVsetkySpanky() {
        spanokDao.vymazVsetkySpanky()
    }
}



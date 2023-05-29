package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.lifecycle.LiveData

class SpanokRepository(private val spanokDao: SpanokDao) {

    val nacitajSpanky: LiveData<List<Spanok>> = spanokDao.nacitajSpanky()

    suspend fun pridajSpanok(spanok: Spanok) {
        spanokDao.pridajSpanok(spanok)
    }

    fun nacitajSpanky() {
        spanokDao.nacitajSpanky()
    }

    suspend fun update(spanok: Spanok) {
        spanokDao.update(spanok)
    }

    suspend fun delete(spanok: Spanok) {
        spanokDao.delete(spanok)
    }

    fun dajPriemerneSkore(): LiveData<Int> {
        return spanokDao.dajPriemerneSkore()
    }

    suspend fun vymazVsetkySpanky() {
        spanokDao.vymazVsetkySpanky()
    }
}



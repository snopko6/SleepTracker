package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.lifecycle.LiveData

class SpanokRepository(private val spanokDao: SpanokDao) {

    val nacitaj: LiveData<List<Spanok>> = spanokDao.nacitajSpanky()

    suspend fun pridajSpanok(spanok: Spanok) {
        spanokDao.pridajSpanok(spanok)
    }

    suspend fun nacitajSpanky() {
        spanokDao.nacitajSpanky()
    }

    suspend fun vymazVsetkySpanky() {
        spanokDao.vymazVsetkySpanky()
    }
}



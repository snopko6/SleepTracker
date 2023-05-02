package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpanokRepository(private val spanokDao: SpanokDao) {

    val nacitaj: LiveData<List<Spanok>> = spanokDao.nacitajSpanky()

    suspend fun pridajSpanok(spanok: Spanok){
        withContext(Dispatchers.IO){
            spanokDao.pridajSpanok(spanok)
        }
    }

    suspend fun nacitajSpanky(): LiveData<List<Spanok>> {
        return withContext(Dispatchers.IO){
            spanokDao.nacitajSpanky()
        }
    }
}
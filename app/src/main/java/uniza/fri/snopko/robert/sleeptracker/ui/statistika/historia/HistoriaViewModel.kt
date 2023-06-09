package uniza.fri.snopko.robert.sleeptracker.ui.statistika.historia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokDatabase
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokRepository

/**
 * ViewModel pre riadenie dát na aktivite Historia
 *
 * @param application inštancia aplikácie
 */
class HistoriaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SpanokRepository
    val spanky: LiveData<List<Spanok>>

    init {
        val spanokDao = SpanokDatabase.getDatabase(application).spanokDao()
        repository = SpanokRepository(spanokDao)
        spanky = repository.nacitajSpanky
    }

    /**
     * Vymaže všetky záznamy o spánkoch z databázy použitím repozitára.
     */
    fun vymazVsetkySpanky() {
        viewModelScope.launch {
            repository.vymazVsetkySpanky()
        }
    }
}
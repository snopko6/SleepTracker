package uniza.fri.snopko.robert.sleeptracker.ui.statistika.historia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokDatabase
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokRepository

class HistoriaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SpanokRepository
    val spanky: LiveData<List<Spanok>>

    init {
        val spanokDao = SpanokDatabase.getDatabase(application).spanokDao()
        repository = SpanokRepository(spanokDao)
        spanky = repository.nacitajSpanky
    }
}
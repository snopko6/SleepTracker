package uniza.fri.snopko.robert.sleeptracker.ui.statistika.historia

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokRepository

class HistoriaViewModel(private val repository: SpanokRepository) : ViewModel() {

    val spanky: LiveData<List<Spanok>> = repository.nacitajSpanky

    fun vymazVsetkySpanky() = viewModelScope.launch {
        repository.vymazVsetkySpanky()
    }
}
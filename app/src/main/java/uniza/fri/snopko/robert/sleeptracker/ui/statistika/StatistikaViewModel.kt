package uniza.fri.snopko.robert.sleeptracker.ui.statistika

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokDatabase
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokRepository

/**
 * ViewModel pre riadenie dát na fragmente StastistikaFragment
 *
 * @param application Inštancia aplikácie
 */
class StatistikaViewModel(application: Application) : AndroidViewModel(application) {

    private val spanokDao = SpanokDatabase.getDatabase(application).spanokDao()
    private val repository = SpanokRepository(spanokDao)

    val priemerneSkore: LiveData<Int> = repository.dajPriemerneSkore()
    val vsetkySpanky: LiveData<List<Spanok>> = repository.nacitajSpanky
}
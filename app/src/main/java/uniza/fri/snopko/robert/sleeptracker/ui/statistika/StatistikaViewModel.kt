package uniza.fri.snopko.robert.sleeptracker.ui.statistika

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokDatabase
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokRepository

class StatistikaViewModel(application: Application) : AndroidViewModel(application) {

    private val spanokDao = SpanokDatabase.getDatabase(application).spanokDao()
    private val repository = SpanokRepository(spanokDao)

    val priemerneSkore: LiveData<Int> = repository.dajPriemerneSkore()
}
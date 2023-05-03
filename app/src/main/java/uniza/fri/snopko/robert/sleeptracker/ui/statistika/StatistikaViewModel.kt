package uniza.fri.snopko.robert.sleeptracker.ui.statistika

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok

class StatistikaViewModel : ViewModel() {

    private val _spanky = MutableLiveData<List<Spanok>>()
    val spanky: LiveData<List<Spanok>>
        get() = _spanky
}
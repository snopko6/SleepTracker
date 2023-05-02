package uniza.fri.snopko.robert.sleeptracker.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok

class DashboardViewModel : ViewModel() {

    private val _spanky = MutableLiveData<List<Spanok>>()
    val spanky: LiveData<List<Spanok>>
        get() = _spanky

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}
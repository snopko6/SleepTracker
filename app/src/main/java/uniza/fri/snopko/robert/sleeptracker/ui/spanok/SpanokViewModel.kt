package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SpanokViewModel : ViewModel() {
    private val _zaciatokSpanku = MutableLiveData<Long>()
    val zaciatokSpanku : MutableLiveData<Long>
        get() = _zaciatokSpanku

    private val _koniecSpanku = MutableLiveData<Long>()
    val koniecSpanku : MutableLiveData<Long>
        get() = _koniecSpanku

    fun setZaciatokSpanku(cas: Calendar){
        _zaciatokSpanku.value = cas.timeInMillis
    }

    fun setKoniecSpanku(cas: Calendar){
        _koniecSpanku.value = cas.timeInMillis
    }
}
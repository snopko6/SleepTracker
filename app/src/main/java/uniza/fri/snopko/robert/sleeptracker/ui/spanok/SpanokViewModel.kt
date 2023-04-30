package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SpanokViewModel : ViewModel() {

    private val _zaciatokSpanku = MutableLiveData<Long>()
    val zaciatokSpanku: MutableLiveData<Long>
        get() = _zaciatokSpanku

    private val _koniecSpanku = MutableLiveData<Long>()
    val koniecSpanku: MutableLiveData<Long>
        get() = _koniecSpanku

//    private val _tlacidloStart = MutableLiveData<Boolean>()
//    val tlacidloStart: LiveData<Boolean>
//        get() = _tlacidloStart

    fun setZaciatokSpanku(cas: Calendar) {
        _zaciatokSpanku.value = cas.timeInMillis
        Log.d("SpanokFragment", "zaciatokSpanku value: ${zaciatokSpanku.value}")
    }

    fun setKoniecSpanku(cas: Calendar) {
        _koniecSpanku.value = cas.timeInMillis
        Log.d("SpanokFragment", "koniecSpanku value: ${koniecSpanku.value}")
    }

//    fun setStartTlacidloStlacene(stlacene: Boolean) {
//        _tlacidloStart.value = stlacene
//    }

}
package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpanokViewModel : ViewModel() {

    private val _zaciatokSpanku = MutableLiveData<Long>()
    val zaciatokSpanku: LiveData<Long>
        get() = _zaciatokSpanku

    private val _koniecSpanku = MutableLiveData<Long>()
    val koniecSpanku: LiveData<Long>
        get() = _koniecSpanku

    private val _tlacidloStartStlacene = MutableLiveData<Boolean>(true)
    val tlacidloStartStlacene: LiveData<Boolean>
        get() = _tlacidloStartStlacene

    private val _tlacidloStopStlacene = MutableLiveData<Boolean>(false)
    val tlacidloStopStlacene: LiveData<Boolean>
        get() = _tlacidloStopStlacene

    fun setZaciatokSpanku(cas: Long) {
        _zaciatokSpanku.value = cas
        Log.d("SpanokFragment", "zaciatokSpanku value: ${zaciatokSpanku.value}")
    }

    fun setKoniecSpanku(cas: Long) {
        _koniecSpanku.value = cas
        Log.d("SpanokFragment", "koniecSpanku value: ${koniecSpanku.value}")
    }

    fun tlacidloStartStlacene() {
        _tlacidloStartStlacene.value = false
        _tlacidloStopStlacene.value = true
    }

    fun tlacidloStopStlacene() {
        _tlacidloStartStlacene.value = true
        _tlacidloStopStlacene.value = false
    }

}
package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokDatabase
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokRepository
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class SpanokViewModel(application: Application) : AndroidViewModel(application) {

    private val nacitaj: LiveData<List<Spanok>>
    private val repository: SpanokRepository
    init {
        val spanokDao = SpanokDatabase.getDatabase(application).spanokDao()
        repository = SpanokRepository(spanokDao)
        nacitaj = repository.nacitaj
    }

    private val _zaciatokSpanku = MutableLiveData<Long>()
    val zaciatokSpanku: LiveData<Long>
        get() = _zaciatokSpanku

    private val _koniecSpanku = MutableLiveData<Long>()
    val koniecSpanku: LiveData<Long>
        get() = _koniecSpanku

    private val _zaciatokSpankuString = MutableLiveData<String>()
    val zaciatokSpankuString: LiveData<String>
        get() = _zaciatokSpankuString

    private val _koniecSpankuString = MutableLiveData<String>()
    val koniecSpankuString: LiveData<String>
        get() = _koniecSpankuString

    private val _tlacidloStartStlacene = MutableLiveData<Boolean>(true)
    val tlacidloStartStlacene: LiveData<Boolean>
        get() = _tlacidloStartStlacene

    private val _tlacidloStopStlacene = MutableLiveData<Boolean>(false)
    val tlacidloStopStlacene: LiveData<Boolean>
        get() = _tlacidloStopStlacene

    private val _isielSpat = MutableLiveData<Long>()
    val isielSpat: LiveData<Long>
        get() = _isielSpat

    private val _zobudilSa = MutableLiveData<Long>(0)
    val zobudilSa: LiveData<Long>
        get() = _zobudilSa

    fun setZaciatokSpanku(cas: Long) {
        _zaciatokSpanku.value = cas
        Log.d("SpanokFragment", "zaciatokSpanku value: ${zaciatokSpanku.value}")
    }

    fun setFormatovanyZaciatokSpanku(formatovanyCas: String) {
        _zaciatokSpankuString.value = formatovanyCas
        Log.d("SpanokFragment", "zaciatokSpankuFormatovany value: ${zaciatokSpankuString.value}")
    }

    fun setKoniecSpanku(cas: Long) {
        _koniecSpanku.value = cas
        Log.d("SpanokFragment", "koniecSpanku value: ${koniecSpanku.value}")
    }

    fun setFormatovanyKoniecSpanku(formatovanyCas: String) {
        _koniecSpankuString.value = formatovanyCas
        Log.d("SpanokFragment", "koniecSpankuFormatovany value: ${koniecSpankuString.value}")
    }

    fun tlacidloStartStlacene() {
        _tlacidloStartStlacene.value = false
        _tlacidloStopStlacene.value = true
        _isielSpat.value = System.currentTimeMillis()
    }

    fun tlacidloStopStlacene() {
        _tlacidloStartStlacene.value = true
        _tlacidloStopStlacene.value = false
        _zobudilSa.value = System.currentTimeMillis()
    }

    fun akoDlhoTrvalSpanok(): Long {
        return _zobudilSa.value!!.toLong().minus(isielSpat.value!!.toLong())
    }

    private fun pridajSpanok(spanok: Spanok){
        viewModelScope.launch(Dispatchers.IO) {
            repository.pridajSpanok(spanok)
        }
    }

    fun vlozitDoDatabazy() {
        val spanok = Spanok(
            0,
            zaciatokSpanku.value!!,
            koniecSpanku.value!!,
            zaciatokSpankuString.value!!,
            koniecSpankuString.value!!,
            isielSpat.value!!,
            zobudilSa.value!!
        )
        pridajSpanok(spanok)
    }

    fun ulozData(nazovSuboru: String, context: Context) {
        val vystupSuboru = context.openFileOutput(nazovSuboru, Context.MODE_PRIVATE)
        val zapisovac = OutputStreamWriter(vystupSuboru)
        zapisovac.write(
            "${zaciatokSpanku.value.toString()},${koniecSpanku.value.toString()}," +
                    "${zaciatokSpankuString.value},${koniecSpankuString.value}," +
                    "${tlacidloStartStlacene.value},${tlacidloStopStlacene.value}" +
                    ",${zobudilSa.value},${isielSpat.value}"
        )
        zapisovac.close()
        vystupSuboru.close()
        Log.d("SpanokFragment", "Data ulozene.")
        Log.d("SpanokFragment", File(context.filesDir, nazovSuboru).readText())
    }

    fun nacitajData(nazovSuboru: String, context: Context) {
        try {
            val vstupSuboru = context.openFileInput(nazovSuboru)
            val citac = InputStreamReader(vstupSuboru)
            val bufferedReader = BufferedReader(citac)
            val hodnoty = bufferedReader.readLine().split(",")

            _zaciatokSpanku.value = hodnoty[0].toLong()
            _koniecSpanku.value = hodnoty[1].toLong()
            _zaciatokSpankuString.value = hodnoty[2]
            _koniecSpankuString.value = hodnoty[3]
            _tlacidloStartStlacene.value = hodnoty[4].toBoolean()
            _tlacidloStopStlacene.value = hodnoty[5].toBoolean()
            _zobudilSa.value = hodnoty[6].toLong()
            _isielSpat.value = hodnoty[7].toLong()
        } catch (e: FileNotFoundException) {
            Log.e("SpanokFragment", "Subor neexistuje!")
        } catch (e: Exception) {
            Log.e("SpanokFragment", "Nastala chyba pri citani suboru $nazovSuboru!", e)
        }
        Log.d("SpanokFragment", "${_zobudilSa.value}, ${_isielSpat.value}")
    }
}

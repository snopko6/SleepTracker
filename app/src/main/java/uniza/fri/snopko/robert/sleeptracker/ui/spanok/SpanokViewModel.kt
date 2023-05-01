package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class SpanokViewModel : ViewModel() {

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

    private var dataNacitane = false

    fun setZaciatokSpanku(cas: Long) {
        _zaciatokSpanku.value = cas
        Log.d("SpanokFragment", "zaciatokSpanku value: ${zaciatokSpanku.value}")
    }

    fun setFormatovanyZaciatokSpanku(formatovanyCas: String){
        _zaciatokSpankuString.value = formatovanyCas
        Log.d("SpanokFragment", "zaciatokSpankuFormatovany value: ${zaciatokSpankuString.value}")
    }

    fun setKoniecSpanku(cas: Long) {
        _koniecSpanku.value = cas
        Log.d("SpanokFragment", "koniecSpanku value: ${koniecSpanku.value}")
    }

    fun setFormatovanyKoniecSpanku(formatovanyCas: String){
        _koniecSpankuString.value = formatovanyCas
        Log.d("SpanokFragment", "koniecSpankuFormatovany value: ${koniecSpankuString.value}")
    }

    fun tlacidloStartStlacene() {
        _tlacidloStartStlacene.value = false
        _tlacidloStopStlacene.value = true
    }

    fun tlacidloStopStlacene() {
        _tlacidloStartStlacene.value = true
        _tlacidloStopStlacene.value = false
    }

    fun ulozData(nazovSuboru: String, context: Context) {
        val vystupSuboru = context.openFileOutput(nazovSuboru, Context.MODE_PRIVATE)
        val zapisovac = OutputStreamWriter(vystupSuboru)
        zapisovac.write("${zaciatokSpanku.value.toString()},${koniecSpanku.value.toString()}," +
                "${zaciatokSpankuString.value},${koniecSpankuString.value},${tlacidloStartStlacene.value},${tlacidloStopStlacene.value}")
        zapisovac.close()
        vystupSuboru.close()
        Log.d("SpanokFragment", "Data ulozene.")
        Log.d("SpanokFragment", File(context.filesDir, nazovSuboru).readText())
    }

    fun nacitajData(nazovSuboru: String, context: Context){
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
        } catch (e: FileNotFoundException){
            Log.e("SpanokFragment","Subor neexistuje!")
        } catch (e: Exception) {
            Log.e("SpanokFragment", "Nastala chyba pri citani suboru $nazovSuboru!", e)
        }
        Log.d("SpanokFragment", "${_koniecSpanku.value}")
        dataNacitane = true
    }
}
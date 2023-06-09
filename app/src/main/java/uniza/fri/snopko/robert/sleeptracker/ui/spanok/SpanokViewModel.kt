package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uniza.fri.snopko.robert.sleeptracker.VypocetHodnotSpanku
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokDatabase
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokRepository
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * Trieda SpanokViewModel na riadenie dát o spánkoch. Pracuje s databázou, a v init block-u sa
 * inicializujú prvky databázy. Používa LiveData a MutableLiveData na ukladanie hodnôt o spánkoch.
 */
class SpanokViewModel(application: Application) : AndroidViewModel(application) {

    private val nacitaj: LiveData<List<Spanok>>
    private val repository: SpanokRepository

    init {
        val spanokDao = SpanokDatabase.getDatabase(application).spanokDao()
        repository = SpanokRepository(spanokDao)
        nacitaj = repository.nacitajSpanky
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

    private val _skore = MutableLiveData(0F)
    val skore: LiveData<Float>
        get() = _skore

    /**
     * Nastaví začiatok spánku
     *
     * @param cas Čas v milisekundách
     */
    fun setZaciatokSpanku(cas: Long) {
        _zaciatokSpanku.value = cas
    }

    /**
     * Nastaví začiatok spánku vo formáte String
     *
     * @param formatovanyCas Naformátovaný čas (HH:mm)
     */
    fun setFormatovanyZaciatokSpanku(formatovanyCas: String) {
        _zaciatokSpankuString.value = formatovanyCas
    }

    /**
     * Nastaví koniec spánku
     *
     * @param cas Čas v milisekundách
     */
    fun setKoniecSpanku(cas: Long) {
        _koniecSpanku.value = cas
    }

    /**
     * Nastaví začiatok spánku vo formáte String
     *
     * @param formatovanyCas Naformátovaný čas (HH:mm)
     */
    fun setFormatovanyKoniecSpanku(formatovanyCas: String) {
        _koniecSpankuString.value = formatovanyCas
    }

    /**
     * Nastaví stavy tlačidiel a času spánku na základe stlačenia tlačidla START
     */
    fun tlacidloStartStlacene() {
        _tlacidloStartStlacene.value = false
        _tlacidloStopStlacene.value = true
        _isielSpat.value = System.currentTimeMillis()
    }

    /**
     * Nastaví stavy tlačidiel a času spánku na základe stlačenia tlačidla STOP. Vypočíta skóre
     * spánku pomocou metódy vypocitajSkore v singletone VypocetHodnotSpanku
     */
    fun tlacidloStopStlacene() {
        _tlacidloStartStlacene.value = true
        _tlacidloStopStlacene.value = false
        _zobudilSa.value = System.currentTimeMillis()
        _skore.value =
            VypocetHodnotSpanku.vypocitajSkore(
                zaciatokSpanku.value!!,
                koniecSpanku.value!!,
                isielSpat.value!!,
                zobudilSa.value!!
            ).toFloat()
    }

    /**
     * Vytvorí inštanciu dátovej triedy Spanok a vloží ju do databázy použitím repozitára.
     */
    fun vlozitDoDatabazy() {
        val spanok = Spanok(
            0,
            zaciatokSpanku.value!!,
            koniecSpanku.value!!,
            zaciatokSpankuString.value!!,
            koniecSpankuString.value!!,
            isielSpat.value!!,
            zobudilSa.value!!,
            skore.value!!
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.pridajSpanok(spanok)
        }
    }

    /**
     * Uloží dáta o spánku do súboru
     *
     * @param nazovSuboru Názov súboru do ktorého dáta ukladáme
     * @param context kontext aktivity
     */
    fun ulozData(nazovSuboru: String, context: Context) {
        val vystupSuboru = context.openFileOutput(nazovSuboru, Context.MODE_PRIVATE)
        val zapisovac = OutputStreamWriter(vystupSuboru)
        zapisovac.write(
            "${zaciatokSpanku.value.toString()},${koniecSpanku.value.toString()}," +
                    "${zaciatokSpankuString.value},${koniecSpankuString.value}," +
                    "${tlacidloStartStlacene.value},${tlacidloStopStlacene.value}" +
                    ",${zobudilSa.value},${isielSpat.value}, ${skore.value}"
        )
        zapisovac.close()
        vystupSuboru.close()
    }

    /**
     * Načíta dáta so spánkami zo súboru
     *
     * @param nazovSuboru Názov súboru z ktorého dáta načítavame
     * @param context kontext aktivity
     */
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
            _skore.value = hodnoty[8].toFloat()
        } catch (e: FileNotFoundException) {
            Log.e("SpanokFragment", "Subor neexistuje!")
        } catch (e: IOException) {
            Log.e("SpanokFragment", "Nastala chyba pri citani suboru $nazovSuboru!", e)
        }
    }
}

package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uniza.fri.snopko.robert.sleeptracker.MILISEKUNDY_V_JEDNOM_DNI
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentSpanokBinding
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Trieda SpanokFragment, ktorý umožní nastaviť čas, začiatok a koniec spánku. Používa
 * SpanokViewModel na riadenie údajov o spánku.
 */
class SpanokFragment : Fragment() {

    private var _binding: FragmentSpanokBinding? = null
    private lateinit var spanokViewModel: SpanokViewModel
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        spanokViewModel = ViewModelProvider(this)[SpanokViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpanokBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Observer pozoruje zmeny v zaciatokSpankuString and aktualizuje text
        spanokViewModel.zaciatokSpankuString.observe(viewLifecycleOwner) {
            binding.casZaciatokSpanku.text = it
        }

        // Observer pozoruje zmeny v koniecSpankuString and aktualizuje text
        spanokViewModel.koniecSpankuString.observe(viewLifecycleOwner) {
            binding.casKoniecSpanku.text = it
        }

        // Observer pozoruje ci je tlacidlo startButton stlacene a na zaklade toho zmeni jeho farbu
        spanokViewModel.tlacidloStartStlacene.observe(viewLifecycleOwner) { stlacene ->
            binding.startButton.isEnabled = stlacene
            binding.startButton.alpha = if (stlacene) 1f else 0.5f
        }

        // Observer pozoruje ci je tlacidlo stopButton stlacene a na zaklade toho zmeni jeho farbu
        spanokViewModel.tlacidloStopStlacene.observe(viewLifecycleOwner) { stlacene ->
            binding.stopButton.isEnabled = stlacene
            binding.stopButton.alpha = if (stlacene) 1f else 0.5f
        }

        // setOnClickListener pre tlacidlo startButton, ktore spusti funkciu tlacidloStartStlacene a
        // zobrazi toast notifikaciu
        binding.startButton.setOnClickListener {
            if (kontrolaNull()) {
                spanokViewModel.tlacidloStartStlacene()
                Toast.makeText(activity, R.string.dobruNoc, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, R.string.prosimVyberte, Toast.LENGTH_SHORT).show()
            }
        }

        // setOnClickListener pre tlacidlo stopButton, ktore spusti funkciu tlacidloStartStlacene a
        // zobrazi toast notifikaciu. Ak je dlzkaSpanku mensi ako nula, t.j. zacal novy den, prida
        // pocet milisekund aby bol vypocet casu spanku spravny
        binding.stopButton.setOnClickListener {
            spanokViewModel.tlacidloStopStlacene()
            var dlzkaSpanku =
                spanokViewModel.zobudilSa.value?.minus(
                    spanokViewModel.isielSpat.value ?: return@setOnClickListener
                )
            if (dlzkaSpanku != null) {
                if (dlzkaSpanku < 0) {
                    dlzkaSpanku += MILISEKUNDY_V_JEDNOM_DNI
                }
            }
            //https://stackoverflow.com/questions/68097384/what-will-be-the-ideal-method-to-convert-the-milliseconds-to-minutes-in-kotlin
            val hodiny = TimeUnit.MILLISECONDS.toHours(dlzkaSpanku!!)
            val minuty = TimeUnit.MILLISECONDS.toMinutes(dlzkaSpanku) % 60
            val spaliSte =
                getString(R.string.spaliSte, "$hodiny hodín, $minuty minút.")

            Toast.makeText(activity, spaliSte, Toast.LENGTH_LONG).show()
            spanokViewModel.vlozitDoDatabazy()
        }

        dlzkaSpankuOnClickListener(binding.casZaciatokSpanku)
        dlzkaSpankuOnClickListener(binding.casKoniecSpanku)

        // Obnovi stav casZaciatokSpanku a casKoniecSpanku ak savedInstanceState nieje null
        savedInstanceState?.let {
            binding.casZaciatokSpanku.text = it.getString("casZaciatokSpanku")
            binding.casKoniecSpanku.text = it.getString("casKoniecSpanku")
        }
    }

    /**
     * Nastaví onClickListener pre daný TextView, a otvorí TimePickerDialog na zvolenie času
     * TimePickerDialog umožní nastaviť čas, ktorý bude formátovaný a zobrazený v dannom TextView.
     * Zvolený čas nastaví pre hodnoty v SpanokViewModel v správnom formáte.
     *
     * @param casSpanku TextView ktorý reprezentuje začiatok alebo koniec spánku
     */
    private fun dlzkaSpankuOnClickListener(casSpanku: TextView) {
        casSpanku.setOnClickListener {
            val kalendar = Calendar.getInstance()
            // nastaví čas na začiatok Unix Epoch, aby sme získali milisekundy a nie milisekundy
            // v konkrétnom dni v ktorom nastavujeme čas spánku
            kalendar.set(Calendar.YEAR, 1970)
            kalendar.set(Calendar.MONTH, Calendar.JANUARY)
            kalendar.set(Calendar.DAY_OF_MONTH, 1)
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hodina, minuta ->
                    kalendar.set(Calendar.HOUR_OF_DAY, hodina)
                    kalendar.set(Calendar.MINUTE, minuta)
                    val zvolenyCas = kalendar.timeInMillis
                    val formatCasu = DateFormat.getTimeFormat(context)
                    casSpanku.text = formatCasu.format(kalendar.time)

                    when (casSpanku.id) {
                        R.id.casZaciatokSpanku -> {
                            spanokViewModel.setZaciatokSpanku(zvolenyCas)
                            spanokViewModel.setFormatovanyZaciatokSpanku(casSpanku.text.toString())
                        }

                        R.id.casKoniecSpanku -> {
                            spanokViewModel.setKoniecSpanku(zvolenyCas)
                            spanokViewModel.setFormatovanyKoniecSpanku(casSpanku.text.toString())
                        }
                    }
                },
                kalendar.get(Calendar.HOUR_OF_DAY),
                kalendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(requireContext())
            )
            timePickerDialog.show()
        }
    }

    /**
     * Kontroluje, či hodnoty zaciatokSpanku a koniecSpanku uložené v SpanokViewModel
     * sa nerovnajú null
     * @return True ak obidve hodnoty nie sú null, false ak jedna z nich je null
     */
    private fun kontrolaNull(): Boolean =
        spanokViewModel.zaciatokSpanku.value != null && spanokViewModel.koniecSpanku.value != null

    /**
     * Uloží aktuálny stav fragmentu. Uloží hodnoty pre zaciatokSpankuString a koniecSpankuString.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("casZaciatokSpanku", spanokViewModel.zaciatokSpankuString.value)
        outState.putString("casKoniecSpanku", spanokViewModel.koniecSpankuString.value)
    }

    /**
     * Pri zastavení tohto fragmentu uloží dáta do textového súboru "data.txt" pomocou funkcie
     * ulozData v SpanokViewModel.
     */
    override fun onStop() {
        super.onStop()
        spanokViewModel.ulozData("data.txt", requireContext())
    }

    /**
     * Pri opätovnom zobrazení fragmentu načíta dáta z textového súboru "data.txt" pomocou funkcie
     * nacitajData v SpanokViewModel.
     */
    override fun onResume() {
        super.onResume()
        spanokViewModel.nacitajData("data.txt", requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
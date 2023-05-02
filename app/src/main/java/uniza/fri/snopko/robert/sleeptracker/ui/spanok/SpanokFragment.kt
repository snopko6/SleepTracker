package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentSpanokBinding
import java.text.SimpleDateFormat
import java.util.*

class SpanokFragment : Fragment() {

    private var _binding: FragmentSpanokBinding? = null
    private lateinit var spanokViewModel: SpanokViewModel
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpanokBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spanokViewModel = ViewModelProvider(this)[SpanokViewModel::class.java]

        val tlacidloStart: Button = view.findViewById(R.id.startButton)
        val tlacidloStop: Button = view.findViewById(R.id.endButton)
        val casZaciatokSpankuTextView: TextView = view.findViewById(R.id.casZaciatokSpanku)
        val casKoniecSpankuTextView: TextView = view.findViewById(R.id.casKoniecSpanku)

        spanokViewModel.zaciatokSpankuString.observe(viewLifecycleOwner) {
            casZaciatokSpankuTextView.text = spanokViewModel.zaciatokSpankuString.value
        }

        spanokViewModel.koniecSpankuString.observe(viewLifecycleOwner) {
            casKoniecSpankuTextView.text = spanokViewModel.koniecSpankuString.value
        }

        spanokViewModel.tlacidloStartStlacene.observe(viewLifecycleOwner) { stlacene ->
            tlacidloStart.isEnabled = stlacene
            tlacidloStart.alpha = if (stlacene) 1f else 0.5f
        }

        spanokViewModel.tlacidloStopStlacene.observe(viewLifecycleOwner) { stlacene ->
            tlacidloStop.isEnabled = stlacene
            tlacidloStop.alpha = if (stlacene) 1f else 0.5f
        }

        tlacidloStart.setOnClickListener {
            if (kontrolaNull()) {
                spanokViewModel.tlacidloStartStlacene()
                Toast.makeText(activity, R.string.dobruNoc, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, R.string.prosimVyberte, Toast.LENGTH_SHORT).show()
            }
        }

        tlacidloStop.setOnClickListener {
            spanokViewModel.tlacidloStopStlacene()
            val formatCasu = SimpleDateFormat("HH:mm", Locale.getDefault())
            val formatovanyCas = formatCasu.format(spanokViewModel.akoDlhoTrvalSpanok())
            val spaliSte = getString(R.string.spaliSte, formatovanyCas)
            Toast.makeText(activity, spaliSte, Toast.LENGTH_LONG).show()
            vlozitDoDatabazy()
        }

        dlzkaSpankuOnClickListener(casZaciatokSpankuTextView)
        dlzkaSpankuOnClickListener(casKoniecSpankuTextView)

        if (savedInstanceState != null) {
            casZaciatokSpankuTextView.text = savedInstanceState.getString("casZaciatokSpanku")
            casKoniecSpankuTextView.text = savedInstanceState.getString("casKoniecSpanku")
        }
    }

    private fun vlozitDoDatabazy() {
        val spanok = Spanok(
            0,
            spanokViewModel.zaciatokSpanku.value!!,
            spanokViewModel.koniecSpanku.value!!,
            spanokViewModel.zaciatokSpankuString.value!!,
            spanokViewModel.koniecSpankuString.value!!,
            spanokViewModel.isielSpat.value!!,
            spanokViewModel.zobudilSa.value!!
        )
        spanokViewModel.pridajSpanok(spanok)
    }

    private fun dlzkaSpankuOnClickListener(dlzkaSpanku: TextView) {
        dlzkaSpanku.setOnClickListener {
            val aktualnyCas = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hodina, minuta ->
                    aktualnyCas.set(Calendar.HOUR_OF_DAY, hodina)
                    aktualnyCas.set(Calendar.MINUTE, minuta)
                    val zvolenyCas = aktualnyCas.timeInMillis
                    val formatCasu = DateFormat.getTimeFormat(requireContext())
                    dlzkaSpanku.text = formatCasu.format(aktualnyCas.time)
                    if (dlzkaSpanku.id == R.id.casZaciatokSpanku) {
                        spanokViewModel.setZaciatokSpanku(zvolenyCas)
                        spanokViewModel.setFormatovanyZaciatokSpanku(dlzkaSpanku.text.toString())
                    } else if (dlzkaSpanku.id == R.id.casKoniecSpanku) {
                        spanokViewModel.setKoniecSpanku(zvolenyCas)
                        spanokViewModel.setFormatovanyKoniecSpanku(dlzkaSpanku.text.toString())
                    }
                },
                aktualnyCas.get(Calendar.HOUR_OF_DAY),
                aktualnyCas.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(requireContext())
            )
            timePickerDialog.show()
        }
    }

    private fun kontrolaNull(): Boolean =
        spanokViewModel.zaciatokSpanku.value != null && spanokViewModel.koniecSpanku.value != null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("casZaciatokSpanku", spanokViewModel.zaciatokSpankuString.value)
        outState.putString("casKoniecSpanku", spanokViewModel.koniecSpankuString.value)
    }

    override fun onStop() {
        super.onStop()
        spanokViewModel.ulozData("data.txt", requireContext())
    }

    override fun onResume() {
        super.onResume()
        spanokViewModel.nacitajData("data.txt", requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
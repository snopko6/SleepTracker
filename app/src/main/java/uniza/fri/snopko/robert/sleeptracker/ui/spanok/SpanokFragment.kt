package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentSpanokBinding
import java.util.*

class SpanokFragment : Fragment() {

    private var _binding: FragmentSpanokBinding? = null
    private lateinit var spanokViewModel: SpanokViewModel
    private val binding get() = _binding!!

    private var tlacidloStartStlacene : Boolean = false
    private var tlacidloStopStlacene : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpanokBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("tlacidloStartStlacene", tlacidloStartStlacene)
        outState.putBoolean("tlacidloStopStlacene", tlacidloStopStlacene)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spanokViewModel = ViewModelProvider(this).get(SpanokViewModel::class.java)

        val tlacidloStart: Button = view.findViewById(R.id.startButton)
        val tlacidloStop: Button = view.findViewById(R.id.endButton)
        tlacidloStop.isEnabled = false

        tlacidloStart.setOnClickListener {
            tlacidloStart.isEnabled = false
            tlacidloStart.alpha = 0.5f
            tlacidloStop.isEnabled = true
            tlacidloStop.alpha = 1f
        }

        tlacidloStop.setOnClickListener {
            tlacidloStop.isEnabled = false
            tlacidloStop.alpha = 0.5f
            tlacidloStart.isEnabled = true
            tlacidloStart.alpha = 1f
        }

        val casZaciatokSpankuTextView: TextView = view.findViewById(R.id.casZaciatokSpanku)
        spanokOnClickListener(casZaciatokSpankuTextView, spanokViewModel.zaciatokSpanku)

        val casKoniecSpankuTextView: TextView = view.findViewById(R.id.casKoniecSpanku)
        spanokOnClickListener(casKoniecSpankuTextView, spanokViewModel.koniecSpanku)
    }

    fun spanokOnClickListener(textView: TextView, kalendarLiveData: MutableLiveData<Long>) {
        textView.setOnClickListener {
            val aktualnyCas = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hodina, minuta ->
                    aktualnyCas.set(Calendar.HOUR_OF_DAY, hodina)
                    aktualnyCas.set(Calendar.MINUTE, minuta)
                    kalendarLiveData.value = aktualnyCas.timeInMillis
                    val formatCasu = DateFormat.getTimeFormat(requireContext())
                    textView.text = formatCasu.format(aktualnyCas.time)
                    if (textView.id == R.id.casZaciatokSpanku) {
                        spanokViewModel.setZaciatokSpanku(aktualnyCas)
                    } else if (textView.id == R.id.casKoniecSpanku) {
                        spanokViewModel.setKoniecSpanku(aktualnyCas)
                    }
                },
                aktualnyCas.get(Calendar.HOUR_OF_DAY),
                aktualnyCas.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(requireContext())
            )
            timePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
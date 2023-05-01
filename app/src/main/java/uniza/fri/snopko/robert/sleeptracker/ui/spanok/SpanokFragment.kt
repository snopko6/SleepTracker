package uniza.fri.snopko.robert.sleeptracker.ui.spanok

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentSpanokBinding
import java.util.*

class SpanokFragment : Fragment() {

    private var _binding: FragmentSpanokBinding? = null
    private lateinit var spanokViewModel: SpanokViewModel
    private val binding get() = _binding!!

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
        spanokViewModel = ViewModelProvider(this).get(SpanokViewModel::class.java)

        val tlacidloStart: Button = view.findViewById(R.id.startButton)
        val tlacidloStop: Button = view.findViewById(R.id.endButton)

        spanokViewModel.tlacidloStartStlacene.observe(viewLifecycleOwner) { stlacene ->
            tlacidloStart.isEnabled = stlacene
            tlacidloStart.alpha = if (stlacene) 1f else 0.5f
        }

        spanokViewModel.tlacidloStopStlacene.observe(viewLifecycleOwner) { stlacene ->
            tlacidloStop.isEnabled = stlacene
            tlacidloStop.alpha = if (stlacene) 1f else 0.5f
        }

        tlacidloStart.setOnClickListener {
            spanokViewModel.tlacidloStartStlacene()
        }

        tlacidloStop.setOnClickListener {
            spanokViewModel.tlacidloStopStlacene()
        }

        val casZaciatokSpankuTextView: TextView = view.findViewById(R.id.casZaciatokSpanku)
        dlzkaSpankuOnClickListener(casZaciatokSpankuTextView)

        val casKoniecSpankuTextView: TextView = view.findViewById(R.id.casKoniecSpanku)
        dlzkaSpankuOnClickListener(casKoniecSpankuTextView)
    }

    private fun dlzkaSpankuOnClickListener(textView: TextView) {
        textView.setOnClickListener {
            val aktualnyCas = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hodina, minuta ->
                    aktualnyCas.set(Calendar.HOUR_OF_DAY, hodina)
                    aktualnyCas.set(Calendar.MINUTE, minuta)
                    val zvolenyCas = aktualnyCas.timeInMillis
                    val formatCasu = DateFormat.getTimeFormat(requireContext())
                    textView.text = formatCasu.format(aktualnyCas.time)
                    if (textView.id == R.id.casZaciatokSpanku) {
                        spanokViewModel.setZaciatokSpanku(zvolenyCas)
                    } else if (textView.id == R.id.casKoniecSpanku) {
                        spanokViewModel.setKoniecSpanku(zvolenyCas)
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
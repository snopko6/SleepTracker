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
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentSpanokBinding
import java.util.*
import java.util.concurrent.TimeUnit

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
        val casZaciatokSpankuTextView: TextView = view.findViewById(R.id.casZaciatokSpanku)
        val casKoniecSpankuTextView: TextView = view.findViewById(R.id.casKoniecSpanku)

        spanokViewModel.zaciatokSpankuString.observe(viewLifecycleOwner) {
            casZaciatokSpankuTextView.text = it
        }

        spanokViewModel.koniecSpankuString.observe(viewLifecycleOwner) {
            casKoniecSpankuTextView.text = it
        }

        spanokViewModel.tlacidloStartStlacene.observe(viewLifecycleOwner) { stlacene ->
            binding.startButton.isEnabled = stlacene
            binding.startButton.alpha = if (stlacene) 1f else 0.5f
        }

        spanokViewModel.tlacidloStopStlacene.observe(viewLifecycleOwner) { stlacene ->
            binding.endButton.isEnabled = stlacene
            binding.endButton.alpha = if (stlacene) 1f else 0.5f
        }

        binding.startButton.setOnClickListener {
            if (kontrolaNull()) {
                spanokViewModel.tlacidloStartStlacene()
                Toast.makeText(activity, R.string.dobruNoc, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, R.string.prosimVyberte, Toast.LENGTH_SHORT).show()
            }
        }

        binding.endButton.setOnClickListener {
            spanokViewModel.tlacidloStopStlacene()
            var dlzkaSpanku =
                spanokViewModel.zobudilSa.value?.minus(spanokViewModel.isielSpat.value!!)
            if (dlzkaSpanku != null) {
                if (dlzkaSpanku < 0) {
                    dlzkaSpanku += 86400000
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

        savedInstanceState?.let {
            binding.casZaciatokSpanku.text = it.getString("casZaciatokSpanku")
            binding.casKoniecSpanku.text = it.getString("casKoniecSpanku")
        }
    }

    private fun dlzkaSpankuOnClickListener(dlzkaSpanku: TextView) {
        dlzkaSpanku.setOnClickListener {
            val kalendar = Calendar.getInstance()
            kalendar.set(Calendar.YEAR, 1970)
            kalendar.set(Calendar.MONTH, Calendar.JANUARY)
            kalendar.set(Calendar.DAY_OF_MONTH, 1)
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hodina, minuta ->
                    kalendar.set(Calendar.HOUR_OF_DAY, hodina)
                    kalendar.set(Calendar.MINUTE, minuta)
                    val zvolenyCas = kalendar.timeInMillis
                    val formatCasu = DateFormat.getTimeFormat(requireContext())
                    dlzkaSpanku.text = formatCasu.format(kalendar.time)

                    when (dlzkaSpanku.id) {
                        R.id.casZaciatokSpanku -> {
                            spanokViewModel.setZaciatokSpanku(zvolenyCas)
                            spanokViewModel.setFormatovanyZaciatokSpanku(dlzkaSpanku.text.toString())
                        }

                        R.id.casKoniecSpanku -> {
                            spanokViewModel.setKoniecSpanku(zvolenyCas)
                            spanokViewModel.setFormatovanyKoniecSpanku(dlzkaSpanku.text.toString())
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
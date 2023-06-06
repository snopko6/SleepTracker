package uniza.fri.snopko.robert.sleeptracker.ui.notifikacie

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentNotificationsBinding

class NotifikacieFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var sharedPreferences: SharedPreferences

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val notifikacieViewModel =
            ViewModelProvider(this)[NotifikacieViewModel::class.java]

        val prepinacNotifikacie = binding.prepinacNotifikacie
        val minutyPredSpankom = binding.minutyPredSpankom
        val minutyPoZobudeni = binding.minutyPoZobudeni
        val casy = resources.getStringArray(R.array.casy)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, casy)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.minutyPredSpankom.adapter = adapter
        binding.minutyPoZobudeni.adapter = adapter

        prepinacNotifikacie.isChecked = sharedPreferences.getBoolean("prepinacNotifikacie", false)
        nastavViditelnost(prepinacNotifikacie.isChecked)
        minutyPredSpankom.setSelection(sharedPreferences.getInt("minutyPredSpankom", 0))
        minutyPoZobudeni.setSelection(sharedPreferences.getInt("minutyPoZobudeni", 0))

        prepinacNotifikacie.setOnCheckedChangeListener { _, jeStlaceny ->
            sharedPreferences.edit().putBoolean("prepinacNotifikacie", jeStlaceny).apply()
            nastavViditelnost(jeStlaceny)
        }
        //https://stackoverflow.com/questions/46447296/android-kotlin-onitemselectedlistener-for-spinner-not-working
        minutyPredSpankom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedPreferences.edit().putInt("minutyPredSpankom", position).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        minutyPoZobudeni.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedPreferences.edit().putInt("minutyPoZobudeni", position).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return binding.root
    }

    private fun nastavViditelnost(viditelne: Boolean) {
        if (viditelne) {
            binding.minutyPredSpankom.visibility = View.VISIBLE
            binding.minutyPoZobudeni.visibility = View.VISIBLE
            binding.predSpanimTextView.visibility = View.VISIBLE
            binding.poZobudeniTextView.visibility = View.VISIBLE
        } else {
            binding.minutyPredSpankom.visibility = View.GONE
            binding.minutyPoZobudeni.visibility = View.GONE
            binding.predSpanimTextView.visibility = View.GONE
            binding.poZobudeniTextView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package uniza.fri.snopko.robert.sleeptracker.ui.notifikacie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentNotificationsBinding

class NotifikacieFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val notifikacieViewModel =
            ViewModelProvider(this)[NotifikacieViewModel::class.java]

        val prepinac = binding.prepinacNotifikacie
        val predSpanimTextView = binding.predSpanimTextView
        val predZobudenimTextView = binding.predZobudenimTextView
        val minutyPredSpankom = binding.minutyPredSpankom
        val minutyPredZobudenim = binding.minutyPredZobudenim
        val casy = resources.getStringArray(R.array.casy)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, casy)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.minutyPredSpankom.adapter = adapter
        binding.minutyPredZobudenim.adapter = adapter

        return binding.root
    }

    private fun nastavViditelnost(viditelne: Boolean) {
        if (viditelne) {
            binding.predSpanimTextView.visibility = View.VISIBLE
            binding.minutyPredZobudenim.visibility = View.VISIBLE
            binding.predSpanimTextView.visibility = View.VISIBLE
            binding.predZobudenimTextView.visibility = View.VISIBLE
        } else {
            binding.minutyPredSpankom.visibility = View.GONE
            binding.minutyPredZobudenim.visibility = View.GONE
            binding.predSpanimTextView.visibility = View.GONE
            binding.predZobudenimTextView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
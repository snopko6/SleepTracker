package uniza.fri.snopko.robert.sleeptracker.ui.statistika

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uniza.fri.snopko.robert.sleeptracker.ui.statistika.historia.Historia
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentStatistikaBinding

class StatistikaFragment : Fragment() {

    private var _binding: FragmentStatistikaBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatistikaBinding.inflate(inflater, container, false)
        val view = _binding?.root

        val statistikaViewModel = ViewModelProvider(this)[StatistikaViewModel::class.java]

        val historiaFloatingButton: FloatingActionButton? = _binding?.historiaFloatingButton

        historiaFloatingButton?.setOnClickListener {
            val intent = Intent(requireActivity(), Historia::class.java)
            startActivity(intent)
        }

        statistikaViewModel.priemerneSkore.observe(viewLifecycleOwner) { skore ->
            _binding?.skore?.text = skore.toString()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
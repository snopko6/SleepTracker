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
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentStatistikaBinding

class StatistikaFragment : Fragment() {

    private var _binding: FragmentStatistikaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val statistikaViewModel =
            ViewModelProvider(this).get(StatistikaViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_statistika, container, false)
        val historiaFloatingButton: FloatingActionButton =
            view.findViewById(R.id.historiaFloatingButton)

        historiaFloatingButton.setOnClickListener {
            val intent = Intent(requireActivity(), Historia::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
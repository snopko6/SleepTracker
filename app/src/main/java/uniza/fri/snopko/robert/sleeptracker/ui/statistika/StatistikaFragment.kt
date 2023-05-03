package uniza.fri.snopko.robert.sleeptracker.ui.statistika

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentStatistikaBinding

class StatistikaFragment : Fragment() {

    private var _binding: FragmentStatistikaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val statistikaViewModel =
            ViewModelProvider(this).get(StatistikaViewModel::class.java)

        _binding = FragmentStatistikaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
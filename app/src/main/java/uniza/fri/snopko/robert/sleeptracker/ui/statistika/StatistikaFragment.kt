package uniza.fri.snopko.robert.sleeptracker.ui.statistika

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok
import uniza.fri.snopko.robert.sleeptracker.ui.statistika.historia.Historia
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentStatistikaBinding

class StatistikaFragment : Fragment() {

    private var _binding: FragmentStatistikaBinding? = null
    private lateinit var graf: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatistikaBinding.inflate(inflater, container, false)
        val view = _binding?.root

        val statistikaViewModel = ViewModelProvider(this)[StatistikaViewModel::class.java]
        val historiaFloatingButton: FloatingActionButton? = _binding?.historiaFloatingButton
        graf = _binding?.graf!!


        historiaFloatingButton?.setOnClickListener {
            val intent = Intent(requireActivity(), Historia::class.java)
            startActivity(intent)
        }

        statistikaViewModel.priemerneSkore.observe(viewLifecycleOwner) { skore ->
            _binding?.skore?.text = skore.toString()
        }

        statistikaViewModel.vsetkySpanky.observe(viewLifecycleOwner) { spanokData ->
            aktualizujGraf(spanokData)
        }
        return view
    }

    private fun aktualizujGraf(spanky: List<Spanok>) {
        val body = ArrayList<Entry>()

        spanky.forEachIndexed{ index, spanok ->
            body.add(Entry(index.toFloat(), spanok.skore))
        }

        val dataSet = LineDataSet(body, "Skore")
        val lineData = LineData(dataSet)
        graf.data = lineData
        graf.description.isEnabled = false
        graf.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
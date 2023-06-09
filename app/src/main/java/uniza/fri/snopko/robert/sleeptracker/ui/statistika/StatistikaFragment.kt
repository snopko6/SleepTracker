package uniza.fri.snopko.robert.sleeptracker.ui.statistika

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.Utils
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok
import uniza.fri.snopko.robert.sleeptracker.databinding.FragmentStatistikaBinding
import uniza.fri.snopko.robert.sleeptracker.ui.statistika.historia.Historia

/**
 * Fragment slúžiaci na zobrazenie štatistiky o spánku. Obsahuje graf, skóre spánku a tlačidlo, ktoré
 * nás dostane na aktivitu Historia
 */
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

        val statistikaViewModel = ViewModelProvider(requireActivity())[StatistikaViewModel::class.java]
        val historiaFloatingButton = _binding?.historiaFloatingButton
        graf = _binding?.graf!!

        // Spustí aktivitu Historia po kliknutí na tlačidlo historiaFloatingButton
        historiaFloatingButton?.setOnClickListener {
            val intent = Intent(requireActivity(), Historia::class.java)
            startActivity(intent)
        }

        // Observer, ktorý sleduje aktuálne skóre spánku. Taktiež mení farbu text na základe
        // výšky skóre
        statistikaViewModel.priemerneSkore.observe(viewLifecycleOwner) { skore ->
            _binding?.skore?.text = skore?.toString() ?: "Žiadne dáta"
            if (skore != null) {
                val skoreUpravenaVaha = (skore.coerceAtLeast(30) - 30) * 100 / 70
                val r = (255 * (100 - skoreUpravenaVaha) / 100)
                val g = (255 * skoreUpravenaVaha / 100)
                val b = 0

                _binding?.skore?.setTextColor(Color.rgb(r, g, b))
            } else {
                _binding?.skore?.setTextColor(Color.BLACK)
            }
        }

        // Observer, ktorý aktualizuje graf na základe zmeny v spánkoch
        statistikaViewModel.vsetkySpanky.observe(viewLifecycleOwner) { spanokData ->
            aktualizujGraf(spanokData)
        }
        return view
    }

    /**
     * Aktualizuje LineChart graf na základe zoznamu o spánkoch. Graf je vykreslený pomocou knižnice
     * MPAndroidChart. Iteruje každý spánok v zozname a pridáho ako Entry do ArrayList-u body, ktorý
     * reprezentuje x a y hodnoty. Riadi dizajn a farby grafu.
     *
     * @param spanky Zoznam spánkov ktorým aktualizuje graf
     */
    private fun aktualizujGraf(spanky: List<Spanok>) {
        val body = ArrayList<Entry>()

        spanky.forEachIndexed { index, spanok ->
            body.add(Entry((index + 1).toFloat(), spanok.skore))
        }

        val dataSet = LineDataSet(body, "Skore").apply {
            lineWidth = 2f
            valueTextSize = 0f
            val textColor = if (zapnutyNocnyRezim()) Color.WHITE else Color.BLACK
            graf.xAxis.textColor = textColor
            graf.axisLeft.textColor = textColor
            color = Color.parseColor("#2F2FCF")
            setDrawFilled(true)
            fillColor = Color.CYAN
            setDrawCircleHole(false)
            setCircleColor(Color.TRANSPARENT)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        //https://stackoverflow.com/questions/32907529/mpandroidchart-fill-color-gradient
        if (Utils.getSDKInt() >= 18) {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_blue)
            dataSet.fillDrawable = drawable
            dataSet.setDrawFilled(true)
        }

        val lineData = LineData(dataSet)
        graf.data = lineData
        graf.description.isEnabled = false
        graf.setTouchEnabled(false)
        graf.isDragEnabled = false
        graf.setScaleEnabled(false)
        graf.setPinchZoom(false)
        graf.legend.isEnabled = false
        graf.axisLeft.setDrawGridLines(false)
        graf.axisLeft.setDrawAxisLine(false)
        graf.axisRight.isEnabled = false
        graf.xAxis.setDrawGridLines(false)
        graf.xAxis.setDrawAxisLine(false)
        graf.invalidate()
    }

    /**
     * Kontroluje, či je na zariadení zapnutý nočný režim
     *
     * @return True ak je zapnutý, false ak je vypnutý
     */
    private fun zapnutyNocnyRezim(): Boolean =
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package uniza.fri.snopko.robert.sleeptracker

import java.text.SimpleDateFormat
import java.util.*

object VypocetHodnotSpanku {

    fun vypocitajDatum(time: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(time * 1000L)
        return sdf.format(date)
    }

    fun vypocitajCas(time: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(time * 1000L)
        return sdf.format(date)
    }

    fun vypocitajSkore(
        zaciatokSpanku: Long,
        koniecSpanku: Long,
        isielSpat: Long,
        zobudilSa: Long
    ): Int {
        val casSpanku = koniecSpanku - zaciatokSpanku
        val skutocnyCasSpanku = zobudilSa - isielSpat
        val priskoro = skutocnyCasSpanku < casSpanku
        val neskoro = skutocnyCasSpanku > casSpanku
        var skore = 100.toDouble()

        if (priskoro) {
            val rozdiel = skutocnyCasSpanku.toDouble() / casSpanku
            skore *= 0.9 * rozdiel
        }

        if (neskoro) {
            val rozdiel = casSpanku.toDouble() / skutocnyCasSpanku
            skore *= 0.75 * rozdiel
        }

        return skore.toInt()
    }

}
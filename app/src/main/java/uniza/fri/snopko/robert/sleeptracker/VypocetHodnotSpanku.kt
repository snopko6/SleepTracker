package uniza.fri.snopko.robert.sleeptracker

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt

object VypocetHodnotSpanku {

    fun vypocitajDatum(time: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(time)
        return sdf.format(date)
    }

    fun vypocitajCas(time: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(time)
        return sdf.format(date)
    }

    fun vypocitajSkore(
        zaciatokSpanku: Long,
        koniecSpanku: Long,
        isielSpat: Long,
        zobudilSa: Long
    ): Int {
        var koniecSpankuUpravene = koniecSpanku

        if (koniecSpankuUpravene < zaciatokSpanku) {
            koniecSpankuUpravene += 24 * 60 * 60 * 1000
        }

        val casSpanku = koniecSpankuUpravene - zaciatokSpanku
        val skutocnyCasSpanku = zobudilSa - isielSpat

        if (skutocnyCasSpanku == casSpanku) {
            return 100
        }

        var skore = 100.toDouble()
        val rozdiel = abs(skutocnyCasSpanku.toDouble() - casSpanku) / casSpanku

        if (skutocnyCasSpanku < 1 * 60 * 60 * 1000) {
            return 0
        }

        skore *= if (skutocnyCasSpanku < casSpanku) {
            (1 - sqrt(0.15 * rozdiel))
        } else {
            (1 - sqrt(0.25 * rozdiel))
        }

        return skore.toInt()
    }
}
package uniza.fri.snopko.robert.sleeptracker

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt

const val MILISEKUNDY_V_HODINE: Long = 60 * 60 * 1000
const val MILISEKUNDY_V_JEDNOM_DNI: Long = 24 * MILISEKUNDY_V_HODINE

/**
 * Objekt na počítanie hodnôt spánku
 */
object VypocetHodnotSpanku {

    /**
     * Získa dátum z času a vráti ho ako String
     *
     * @param cas Čas v milisekundách
     * @return Formátovaný dátum (dd/MM/yyyy)
     */
    fun vypocitajDatum(cas: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val datum = Date(cas)
        return sdf.format(datum)
    }

    /**
     * Získa čas a vráti ho ako String
     *
     * @param cas Čas v milisekundách
     * @return Formátovaný čas (HH:mm)
     */
    fun vypocitajCas(cas: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val datum = Date(cas)
        return sdf.format(datum)
    }

    /**
     * Počíta skóre spánku na základe hodnôt spánku.
     *
     * @param zaciatokSpanku Začiatok spánku v milisekundách
     * @param koniecSpanku Koniec spánku v milisekundách
     * @param isielSpat Čas v milisekundách keď šiel používateľ spať
     * @param zobudilSa Čas v milisekundách keď sa používateľ zobudil
     * @return Vypočítané skóre spánku
     */
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
            (1 - sqrt(0.25 * rozdiel))
        } else {
            (1 - sqrt(0.35 * rozdiel))
        }

        return skore.toInt()
    }
}
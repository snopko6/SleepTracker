package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Dátová trieda reprezentujúca záznam o spánku.
 *
 * @param id Identifikátor spánku
 * @param zaciatokSpanku Začiatok spánku v milisekundách
 * @param koniecSpanku Koniec spánku v milisekundách
 * @param zaciatokSpankuString Formátovaný začiatok spánku (HH:mm)
 * @param koniecSpankuString Formátovaný koniec spánku (HH:mm)
 * @param casStlacilStartTlacidlo Čas v milisekundách kedy používateľ stlačil tlačidlo START
 * @param casStlacilStopTlacidlo Čas v milisekundách kedy používateľ stlačil tlačidlo STOP
 */
@Entity(tableName = "zoznam_spankov")
data class Spanok(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val zaciatokSpanku: Long,
    val koniecSpanku: Long,
    val zaciatokSpankuString: String,
    val koniecSpankuString: String,
    val casStlacilStartTlacidlo: Long,
    val casStlacilStopTlacidlo: Long,
    val skore: Float
)
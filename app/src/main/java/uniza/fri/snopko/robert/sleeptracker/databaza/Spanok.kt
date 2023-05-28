package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.room.Entity
import androidx.room.PrimaryKey

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
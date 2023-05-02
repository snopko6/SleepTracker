package uniza.fri.snopko.robert.sleeptracker.databaza

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zoznam_spankov")
data class Spanok(
    val zaciatokSpanku: Long,
    val koniecSpanku: Long,
    val casStlacilStartTlacidlo: Long,
    val casStlacilStopTlacidlo: Long,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
)
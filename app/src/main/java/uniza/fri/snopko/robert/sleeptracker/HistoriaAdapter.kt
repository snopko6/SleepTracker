package uniza.fri.snopko.robert.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uniza.fri.snopko.robert.sleeptracker.databaza.Spanok

class HistoriaAdapter : RecyclerView.Adapter<HistoriaAdapter.HistoriaViewHolder>() {

    private var spanokList = emptyList<Spanok>()

    class HistoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val datumSpanku: TextView = itemView.findViewById(R.id.datumSpanku)
        val bodySpanku: TextView = itemView.findViewById(R.id.bodySpanku)
        val zaciatokSpanku: TextView = itemView.findViewById(R.id.zaciatokSpanku)
        val koniecSpanku: TextView = itemView.findViewById(R.id.koniecSpanku)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriaViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.spanok_recyclerview, parent, false)
        return HistoriaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoriaViewHolder, position: Int) {
        TODO()
    }

    override fun getItemCount(): Int {
        return spanokList.size
    }
}
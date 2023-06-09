package uniza.fri.snopko.robert.sleeptracker.ui.statistika.historia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uniza.fri.snopko.robert.sleeptracker.R
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokDatabase.Companion.getDatabase
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokRepository

/*
https://developer.android.com/develop/ui/views/components/menus
https://developer.android.com/reference/android/app/ActionBar
*/

/**
 * Aktivita na zobrazovanie historie spánkov. Obsahuje RecyclerView widget na ktorom sa nachádza
 * záznam spánku s dátumom, časom začiatku a konca spánku, a skóre pre daný spánok
 */
class Historia : AppCompatActivity() {

    private lateinit var repository: SpanokRepository
    private lateinit var historiaViewModel: HistoriaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historia)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.historiaNazov)
        }
        val vymazatHistoriuButton = findViewById<Button>(R.id.vymazatHistoriuButton)
        val spanokDao = getDatabase(this).spanokDao()
        repository = SpanokRepository(spanokDao)
        historiaViewModel = ViewModelProvider(this)[HistoriaViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.zoznamSpankov)
        val adapter = HistoriaAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        historiaViewModel.spanky.observe(this) { spanok ->
            adapter.setData(spanok)
        }

        // OnClickListener ktorý riadi tlačidlo ktoré premaže databázu. Pomocou AlertDialog-u sa
        // zobrazí okno ktoré upozorní o vymazaní databázy
        vymazatHistoriuButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.warningNazov))
                .setMessage(getString(R.string.warningText))
                .setPositiveButton(getString(R.string.warningVymazat)) { _, _ ->
                    historiaViewModel.vymazVsetkySpanky()
                    Toast.makeText(
                        this@Historia,
                        "Databáza bola premazaná!",
                        Toast.LENGTH_LONG
                    ).show()
                    adapter.setData(emptyList())
                }
                .setNegativeButton(getString(R.string.warningZrusit), null)
                .show()
        }
    }

    /*
    https://stackoverflow.com/questions/70319639/android-studio-override-fun-onoptionsitemselected-not-navigating-to-second
     */

    /**
     * Riadi odídenie z aktivity pomocou šípky na hornej lište
     *
     * @param item Zvolená položka v menu
     * @return True, ak sa s položkou manipuluje, inak false
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
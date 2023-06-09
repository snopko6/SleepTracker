package uniza.fri.snopko.robert.sleeptracker

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import uniza.fri.snopko.robert.sleeptracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        supportActionBar?.title = "Sleep Tracker"
        navView.setupWithNavController(navController)
        aktualizujWidget(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        aktualizujWidget(this)
    }
    // https://dev.to/tkuenneth/updating-widgets-introduction-4cof
    /**
     * Aktualizuje SkoreSpankuWidget miniaplikáciu zavolaním onUpdate() metódy.
     *
     * @param context Kontext aplikácie
     */
    private fun aktualizujWidget(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisWidget = ComponentName(context, SkoreSpankuWidget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        val widget = SkoreSpankuWidget()
        widget.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}
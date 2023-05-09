package uniza.fri.snopko.robert.sleeptracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

/*
https://developer.android.com/develop/ui/views/components/menus
https://developer.android.com/reference/android/app/ActionBar
*/

class Historia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historia)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.historiaNazov)
        }
    }

    /*
    https://stackoverflow.com/questions/70319639/android-studio-override-fun-onoptionsitemselected-not-navigating-to-second
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
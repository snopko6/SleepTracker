package uniza.fri.snopko.robert.sleeptracker

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokDatabase
import uniza.fri.snopko.robert.sleeptracker.databaza.SpanokRepository

class SkoreSpankuWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val database = SpanokDatabase.getDatabase(context)
            val repository = SpanokRepository(database.spanokDao())
            val skore = repository.dajPriemerneSkoreInt()

            val component = ComponentName(context, SkoreSpankuWidget::class.java)
            val widgets = appWidgetManager.getAppWidgetIds(component)
            for (widgetId in widgets) {
                updateAppWidget(context, appWidgetManager, widgetId, skore)
            }
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            skore: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.skore_widget)
            views.setTextViewText(R.id.skore, skore.toString())
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

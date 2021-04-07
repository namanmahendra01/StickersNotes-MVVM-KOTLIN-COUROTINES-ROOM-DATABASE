package com.naman.stickernotes

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [StickerWidgetConfigureActivity]
 */
class StickerWidget : AppWidgetProvider() {

    val context = this



    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }


    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val PREF_PREFIX_KEY = "appwidget_"


    //    fetch noteid from pref
    val PREFS_NAME = "com.naman.stickernotes.StickerWidget"
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val noteId = prefs.getString("noteid_" + appWidgetId, "fuck")

    val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.sticker_widget)
    views.setTextViewText(R.id.appwidget_text, titleValue)

    val intent = Intent(context, NoteOpen::class.java)
        intent.putExtra("id", noteId)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)

    views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
    appWidgetManager.updateAppWidget(appWidgetId, views)


}
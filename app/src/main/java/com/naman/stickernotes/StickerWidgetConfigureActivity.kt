package com.naman.stickernotes

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RemoteViews
import androidx.recyclerview.widget.LinearLayoutManager
import com.naman.stickernotes.Data.Note
import com.naman.stickernotes.Data.NoteDatabase
import kotlinx.android.synthetic.main.sticker_widget_configure.*

/**
 * The configuration screen for the [StickerWidget] AppWidget.
 */
class StickerWidgetConfigureActivity : Activity(),NotesAdapter.InNotesRvAdapter {
    var selectedNote:String =""
    var noteId:Int=0

//    //    fetch noteid from pref
//    val PREFS_NAME = "com.naman.stickernotes.StickerWidget"
//    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
//    val noteId = prefs.getString("noteid_" + appWidgetId, null)
//
//    // Construct the RemoteViews object
//    val widgetText = loadTitlePref(context, appWidgetId)
//    val views = RemoteViews(context.packageName, R.layout.sticker_widget)
//    views.setTextViewText(R.id.appwidget_text, widgetText)
//
//    // Instruct the widget manager to update the widget
//    appWidgetManager.updateAppWidget(appWidgetId, views)
//
//    //  click listener on widget
//    val intent = Intent(context, NoteOpen::class.java)
//    intent.putExtra("id", noteId)
//    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//    views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent)


    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var appWidgetText: EditText
    private var onClickListener = View.OnClickListener {

        val context = this

        Log.d(TAG, "ninini: "+noteId+"   "+selectedNote)
        saveTitlePref(context, appWidgetId, selectedNote,noteId)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        setContentView(R.layout.sticker_widget_configure)
        findViewById<View>(R.id.addWidgetBtn).setOnClickListener(onClickListener)

        val context = this

        noteRv.layoutManager = LinearLayoutManager(this)
        val noteAdapter=NotesAdapter(this, this, "not from main")
        noteRv.adapter=noteAdapter

        val dao= NoteDatabase.getDataBase(context).getNoteDao()

        val repository =  NoteRepository(dao)
        val list: List<Note> =repository.notesTittle
        noteAdapter.updateList(list)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

//        appWidgetText.setText(loadTitlePref(this@StickerWidgetConfigureActivity, appWidgetId))
    }

    override fun onItemClicked(note: Note?,action:String) {
        if (note != null) {
            selectedNote=note.text
            noteId=note.id

        }else{
            selectedNote=""
            noteId=0
        }
    }

}

private const val PREFS_NAME = "com.naman.stickernotes.StickerWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String, noteId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
    Log.d(TAG, "saveTitlePref: "+noteId)
    prefs.putString("noteid_" + appWidgetId, noteId.toString())
    prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
    Log.d(TAG, "loadTitlePref: "+"noteid_" +  prefs.getString("noteid_" + appWidgetId, null))
    return titleValue ?: context.getString(R.string.appwidget_text)
}


internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.remove("noteid_" + appWidgetId)

    prefs.apply()
}
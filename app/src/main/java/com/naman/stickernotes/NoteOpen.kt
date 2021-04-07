package com.naman.stickernotes

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.naman.stickernotes.Data.Note
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_note.*
import kotlinx.android.synthetic.main.item_note.delete
import kotlinx.android.synthetic.main.snippet_bar.*
import kotlinx.android.synthetic.main.snippet_note_bar.*

class NoteOpen : AppCompatActivity() {

    var noteId: String? = null
    private val TAG = "NoteOpen"
    var show: Boolean = true
    lateinit var viewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_open)

        val i: Intent = intent
        var note: Note? = null
        noteId = i.getStringExtra("id")


        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        if (noteId != null) {
            note = viewModel.noteById(noteId!!.toInt())
            if(note!=null){
                textEt.setText(note.text)
                fulltextEt.setText(note.fullNote)
                header.text = note.text
            }else{
                show=false
                val x = Intent(this, MainActivity::class.java)
                    startActivity(x)
            }


        }


        backarrow.setOnClickListener {

            if (noteId != null) {
                viewModel.updateNote(textEt.text.toString(), fulltextEt.text.toString(), noteId!!.toInt()).invokeOnCompletion {

                    this.runOnUiThread(Runnable {
                        Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show()
                    })
                    finish()

                }
            }
        }
        delete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete this note? ")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog: DialogInterface?, id: Int ->
                        noteId?.let { it1 -> viewModel.noteById(it1.toInt()) }?.let { it2 ->
                            viewModel.deleteNote(it2).invokeOnCompletion {
                                this.runOnUiThread(Runnable {
                                    Toast.makeText(this, "Note Deleted!", Toast.LENGTH_SHORT).show()
                                })
                                show = false
                                val i = Intent(this, MainActivity::class.java)
                                startActivity(i)

                            }
                        }
                    }.setNegativeButton("Cancel") { dialog: DialogInterface?, id: Int ->

                        builder.setCancelable(true)
                    }
                    .show()


        }


    }

    override fun onPause() {
        super.onPause()

        if (show) {
            viewModel.updateNote(textEt.text.toString(), fulltextEt.text.toString(), noteId!!.toInt()).invokeOnCompletion {
                this.runOnUiThread(Runnable {
                    Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show()
                })
                finish()


            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (show) {
            if (noteId != null) {
                viewModel.updateNote(textEt.text.toString(), fulltextEt.text.toString(), noteId!!.toInt()).invokeOnCompletion {
                    this.runOnUiThread(Runnable {
                        Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show()
                    })

                    finish()


                }
            }
        }
    }
}
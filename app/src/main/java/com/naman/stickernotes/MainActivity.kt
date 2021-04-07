package com.naman.stickernotes

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.naman.stickernotes.Data.Note
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.snippet_bar.*


class MainActivity : AppCompatActivity(), NotesAdapter.InNotesRvAdapter {

    private val TAG = "MainActivity"
    lateinit var viewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearM: LinearLayoutManager = LinearLayoutManager(this)

        linearM.reverseLayout = true
        linearM.stackFromEnd = true
        recyclerView.layoutManager = linearM

        val noteAdapter = NotesAdapter(this, this, "from main")
        recyclerView.adapter = noteAdapter


        viewModel = ViewModelProvider(
                this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application
        )
        ).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer {

            if (it != null) {
                noteAdapter.updateList(it)
            }
        })

        widgetBtn.setOnClickListener {
            val i = Intent(this, ViewHelp::class.java)
                startActivity(i)
        }
    }


    override fun onItemClicked(note: Note?, action: String) {


        if (action == "intent_action") {
            val i = Intent(this, NoteOpen::class.java)
            if (note != null) {
                i.putExtra("id", note.id.toString())
                startActivity(i)
            }

        }
        if (action == "for_delete") {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete this note? ")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog: DialogInterface?, id: Int ->
                        if (note != null) {
                            viewModel.deleteNote(note).invokeOnCompletion {
                                this.runOnUiThread(Runnable {
                                    Toast.makeText(this, "Note Deleted!", Toast.LENGTH_SHORT).show()
                                })
                            }
                        }
                    }.setNegativeButton("Cancel") { dialog: DialogInterface?, id: Int ->

                        builder.setCancelable(true)
                    }
                    .show()

        }

    }

    fun submitNote(view: View) {
        val noteText = textEt.text.toString()
        val fullText = fulltextEt.text.toString()
        if (noteText.isNotEmpty() && fullText.isNotEmpty()) {

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to add this note? ")
                    .setCancelable(true)
                    .setPositiveButton("Yes") { dialog: DialogInterface?, id: Int ->

                        viewModel.insertNote(Note(noteText, fullText)).invokeOnCompletion {
                            this.runOnUiThread(Runnable {
                                textEt.text.clear()
                                fulltextEt.text.clear()
                            })


                        }
                        Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show()
                    }.setNegativeButton("Cancel") { dialog: DialogInterface?, id: Int ->

                        builder.setCancelable(true)
                    }
                    .show()
        } else {
            Toast.makeText(this, "Enter Both Fields", Toast.LENGTH_SHORT).show()

        }


    }
}
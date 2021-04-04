package com.naman.stickernotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.naman.stickernotes.Data.Note
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NotesAdapter.InNotesRvAdapter {

    lateinit var viewModel: NoteViewModel
    val TAG="naman"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView.layoutManager =LinearLayoutManager(this)
        val noteAdapter=NotesAdapter(this,this,"from main")
        recyclerView.adapter=noteAdapter

        viewModel=ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer {
            Log.d(TAG, "onCreate: "+it)

            if (it != null) {
                Log.d(TAG, "onCreate: "+it)
                noteAdapter.updateList(it)
            }
        })
    }


    override fun onItemClicked(note: Note?) {

        if (note != null) {
            viewModel.deleteNote(note)
        }
    }

    fun submitNote(view: View) {
        val noteText=textEt.text.toString()
        if(noteText.isNotEmpty()){
            viewModel.insertNote(Note(noteText))
            Toast.makeText(this,"inserted",Toast.LENGTH_SHORT).show()
            Log.d(TAG, "submitNote: "+viewModel.allNotes.value)
        }

    }
}
package com.naman.stickernotes

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.naman.stickernotes.Data.Note
import com.naman.stickernotes.Data.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    val allNotes:LiveData<List<Note>>

    private val repository:NoteRepository
    init {
        val dao=NoteDatabase.getDataBase(application).getNoteDao()
         repository= NoteRepository(dao)
        allNotes=repository.allNotes
    }
    fun noteById(id:Int):Note {
        return repository.noteById(id)
    }

    fun deleteNote(note:Note)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }
    fun insertNote(note:Note)=viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }
    fun updateNote(title:String,fulltext:String,id:Int)=viewModelScope.launch(Dispatchers.IO) {
        repository.update(title, fulltext, id)
    }
}
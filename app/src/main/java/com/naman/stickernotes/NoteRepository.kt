package com.naman.stickernotes

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import com.naman.stickernotes.Data.Note
import com.naman.stickernotes.Data.NoteDao

class NoteRepository(private val noteDao:NoteDao) {

    val allNotes:LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note:Note){

        noteDao.insert(note)
        Log.d(ContentValues.TAG, "insertNote: 2")

    }

    suspend fun delete(note:Note){
        noteDao.delete(note)

    }

}
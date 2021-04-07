package com.naman.stickernotes

import androidx.lifecycle.LiveData
import com.naman.stickernotes.Data.Note
import com.naman.stickernotes.Data.NoteDao

class NoteRepository(private val noteDao:NoteDao) {

    val allNotes:LiveData<List<Note>> = noteDao.getAllNotes()

    val notesTittle:List<Note> = noteDao.getNotesTittle()


    fun noteById(id:Int):Note{
        return noteDao.getNote(id)

    }
    suspend fun insert(note:Note){

        noteDao.insert(note)


    }

    suspend fun delete(note:Note){
        noteDao.delete(note)

    }
    suspend fun update(title:String,fulltext:String,id:Int){
        noteDao.updateNote(title,fulltext,id)

    }

}
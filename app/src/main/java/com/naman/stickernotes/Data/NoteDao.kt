package com.naman.stickernotes.Data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note:Note)
    @Delete
   suspend fun delete(note:Note)

    @Query("Select * from notes_table order by id ASC")
    fun getAllNotes():LiveData<List<Note>>

    @Query("Select * from notes_table")
    fun getNotesTittle():List<Note>




}
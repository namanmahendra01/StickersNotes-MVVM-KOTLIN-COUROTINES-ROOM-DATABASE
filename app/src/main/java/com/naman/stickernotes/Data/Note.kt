package com.naman.stickernotes.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="notes_table")
class Note(@ColumnInfo(name="note_text")val text:String,@ColumnInfo(name="full_note")val fullNote:String) {
    @PrimaryKey(autoGenerate = true) var id=0;
}
package com.example.note.cache.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.note.model.enums.NoteState

@Entity(tableName = "note_table")
data class NoteEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "body")
    var body: String,

    @ColumnInfo(name = "updated_at")
    var updated_at: String,

    @ColumnInfo(name = "created_at")
    var created_at: String,

    @ColumnInfo(name = "state")
    var state: NoteState
)
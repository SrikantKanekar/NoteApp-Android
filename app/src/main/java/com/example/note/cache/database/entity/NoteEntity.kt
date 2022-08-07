package com.example.note.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.note.model.enums.NoteState

@Entity(tableName = "note_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val body: String,
    val updated_at: String,
    val created_at: String,
    val state: NoteState,
    val pinned: Boolean,
    val color: Int,
    val labels: String
)
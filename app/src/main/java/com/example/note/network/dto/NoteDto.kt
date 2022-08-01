package com.example.note.network.dto

import com.example.note.model.enums.NoteState
import com.example.note.model.enums.NoteState.*

data class NoteDto(
    val id: String,
    val title: String,
    val body: String,
    val updated_at: String,
    val created_at: String,
    val state: NoteState?
)
package com.example.note.model

data class NoteGrid(
    val notes: List<Note>,
    val label: String? = null
)

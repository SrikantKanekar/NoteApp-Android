package com.example.note.presentation.ui.notes

import com.example.note.model.Note
import java.io.Serializable

data class NotesUiState(
    val selectedNotes: List<Note> = listOf(),
    val notePendingDelete: NotePendingDelete? = null,
    val errorMessage: String? = null
) : Serializable {

    data class NotePendingDelete(
        val note: Note? = null,
        val listPosition: Int? = null
    ) : Serializable
}
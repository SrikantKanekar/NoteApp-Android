package com.example.note.presentation.ui.notes

import com.example.note.model.Note
import com.example.note.presentation.ui.notes.CardLayoutType.STAGGERED
import java.io.Serializable

data class NotesUiState(
    val notes: List<Note> = listOf(),
    val selectedNotes: List<Note> = listOf(),
    val cardLayoutType: CardLayoutType = STAGGERED,
    val notePendingDelete: NotePendingDelete? = null,
    val errorMessage: String? = null
) : Serializable {

    data class NotePendingDelete(
        val note: Note? = null,
        val listPosition: Int? = null
    ) : Serializable
}

val NotesUiState.isSelectMode: Boolean get() = selectedNotes.isNotEmpty()

val NotesUiState.selectCount: Int get() = selectedNotes.size
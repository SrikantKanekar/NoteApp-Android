package com.example.note.presentation.ui.notes

import com.example.note.model.Note
import com.example.note.model.NoteGrid
import com.example.note.model.enums.CardLayoutType
import com.example.note.model.enums.CardLayoutType.STAGGERED
import com.example.note.model.enums.PageState
import com.example.note.model.enums.PageState.*
import java.io.Serializable

data class NotesUiState(
    val noteGrids: List<NoteGrid> = listOf(),
    val selectedNotes: List<Note> = listOf(),
    val cardLayoutType: CardLayoutType = STAGGERED,
    val notePendingDelete: NotePendingDelete? = null,
    val pageState: PageState = NOTE,
    val isSearch: Boolean = false,
    val query: String = "",
    val errorMessage: String? = null
) : Serializable {

    data class NotePendingDelete(
        val note: Note? = null,
        val listPosition: Int? = null
    ) : Serializable
}

val NotesUiState.isSelectMode: Boolean get() = selectedNotes.isNotEmpty()

val NotesUiState.selectCount: Int get() = selectedNotes.size
package com.example.note.presentation.ui.noteList

import com.example.note.model.Note
import com.example.note.presentation.ui.noteList.NoteListViewModel.NoteListToolbarState
import java.io.Serializable

data class NoteListUiState(
    val noteList: List<Note> = listOf(),
    val selectedNotes: List<Note> = listOf(),
    val notePendingDelete: NotePendingDelete? = null, // set when delete is pending (can be undone)
    val searchQuery: String = "",
    val toolbarState: NoteListToolbarState = NoteListToolbarState.SearchViewState,
    val errorMessage: String? = null
) : Serializable {

    data class NotePendingDelete(
        val note: Note? = null,
        val listPosition: Int? = null
    ) : Serializable
}

val NoteListUiState.isLoading: Boolean get() = false

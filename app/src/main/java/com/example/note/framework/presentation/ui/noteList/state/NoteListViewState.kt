package com.example.note.framework.presentation.ui.noteList.state

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.ViewState
import com.example.note.framework.presentation.ui.noteList.NoteListViewModel.NoteListToolbarState
import java.io.Serializable

data class NoteListViewState(

    var searchQuery: String? = null,
    var notePendingDelete: NotePendingDelete? = null, // set when delete is pending (can be undone)
    var selectedNotes: ArrayList<Note>? = null,
    var toolbarState: NoteListToolbarState? = null

) : ViewState, Serializable {

    data class NotePendingDelete(
        var note: Note? = null,
        var listPosition: Int? = null
    ) : Serializable
}
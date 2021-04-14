package com.example.note.framework.presentation.ui.noteList.state

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.ViewState
import com.example.note.framework.presentation.ui.noteList.NoteListViewModel.NoteListToolbarState

data class NoteListViewState(

    var noteList: ArrayList<Note>? = null,
    var newNote: Note? = null, // note that can be created with fab
    var notePendingDelete: NotePendingDelete? = null, // set when delete is pending (can be undone)
    var searchQuery: String? = null,
    var numNotesInCache: Int? = null,
    var selectedNotes: ArrayList<Note>? = null,
    var toolbarState: NoteListToolbarState? = null

): ViewState {

    data class NotePendingDelete(
        var note: Note? = null,
        var listPosition: Int? = null
    )
}
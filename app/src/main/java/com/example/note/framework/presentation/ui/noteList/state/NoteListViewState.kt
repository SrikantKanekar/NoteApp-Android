package com.example.note.framework.presentation.ui.noteList.state

import android.os.Parcelable
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.ViewState

data class NoteListViewState(

    var noteList: ArrayList<Note>? = null,
    var newNote: Note? = null, // note that can be created with fab
    var notePendingDelete: NotePendingDelete? = null, // set when delete is pending (can be undone)
    var searchQuery: String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var filter: String? = null,
    var order: String? = null,
    var layoutManagerState: Parcelable? = null,
    var numNotesInCache: Int? = null

): ViewState {

    data class NotePendingDelete(
        var note: Note? = null,
        var listPosition: Int? = null
    )
}
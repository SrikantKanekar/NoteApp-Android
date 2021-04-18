package com.example.note.business.interactors.notelist

import com.example.note.business.interactors.common.DeleteNote
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState

// Use cases
class NoteListInteractors (
    val insertNewNote: InsertNewNote,
    val insertMultipleNotes: InsertMultipleNotes, // for testing
    val deleteNote: DeleteNote<NoteListViewState>,
    val searchNotes: SearchNotes,
    val restoreDeletedNote: RestoreDeletedNote,
    val deleteMultipleNotes: DeleteMultipleNotes
)















package com.example.note.business.interactors.noteList

import com.example.note.business.interactors.common.DeleteNote
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState

// Use cases
class NoteListInteractors (
    val insertNewNote: InsertNewNote,
    val deleteNote: DeleteNote<NoteListViewState>,
//    val searchNotes: SearchNotes,
    val getNumNotes: GetNumNotes,
//    val restoreDeletedNote: RestoreDeletedNote,
//    val deleteMultipleNotes: DeleteMultipleNotes,
//    val insertMultipleNotes: InsertMultipleNotes // for testing
)

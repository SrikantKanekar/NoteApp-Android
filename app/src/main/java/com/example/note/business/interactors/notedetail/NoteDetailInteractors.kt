package com.example.note.business.interactors.notedetail

import com.example.note.business.interactors.common.DeleteNote
import com.example.note.framework.presentation.ui.noteDetail.state.NoteDetailViewState

// Use cases
class NoteDetailInteractors (
    val getNote: GetNote,
    val deleteNote: DeleteNote<NoteDetailViewState>,
    val updateNote: UpdateNote
)















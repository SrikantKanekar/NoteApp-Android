package com.example.note.framework.presentation.ui.noteDetail.state

import com.example.note.business.domain.model.Note

data class NoteDetailViewState(
    var note: Note? = null,
    var title: String? = null,
    var body: String? = null
)

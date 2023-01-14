package com.example.note.network.requests

import com.example.note.network.dto.NoteDto

data class NoteInsertOrUpdateRequest(
    val notes: List<NoteDto>
)
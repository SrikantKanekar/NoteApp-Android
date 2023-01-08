package com.example.note.network.dto

import com.example.note.model.enums.NoteState

data class NoteDto(
    val id: String,
    val title: String,
    val body: String,
    val updatedAt: String,
    val createdAt: String,
    val state: NoteState?,
    val pinned: Boolean?,
    val color: Int?,
    val labels: List<String>?
)
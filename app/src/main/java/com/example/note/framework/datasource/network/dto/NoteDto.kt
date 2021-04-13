package com.example.note.framework.datasource.network.dto

data class NoteDto(
    val id: String,
    val title: String,
    val body: String,
    val updated_at: String,
    val created_at: String
)
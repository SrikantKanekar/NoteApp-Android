package com.example.note.business.data.cache

import com.example.note.business.domain.model.Note

interface NoteCacheDataSource {

    suspend fun insertNote(note: Note): Long

    suspend fun insertNotes(notes: List<Note>): LongArray

    suspend fun updateNote(
        primaryKey: String,
        newTitle: String,
        newBody: String?,
        timestamp: String?
    ): Int

    suspend fun searchNoteById(id: String): Note?

    suspend fun getNumNotes(): Int

    suspend fun getAllNotes(): List<Note>

    suspend fun deleteNote(primaryKey: String): Int

    suspend fun deleteNotes(notes: List<Note>): Int

    suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note>
}

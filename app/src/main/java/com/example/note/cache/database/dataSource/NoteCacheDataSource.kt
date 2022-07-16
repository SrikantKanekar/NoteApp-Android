package com.example.note.cache.database.dataSource

import com.example.note.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteCacheDataSource {

    suspend fun insertNote(note: Note)

    suspend fun insertNotes(notes: List<Note>)

    suspend fun updateNote(note: Note)

    suspend fun updateNotes(notes: List<Note>)

    suspend fun getNote(id: String): Note?

    suspend fun getAllNotes(): List<Note>

    suspend fun deleteNote(id: String)

    suspend fun deleteNotes(ids: List<String>)

    fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<List<Note>>
}

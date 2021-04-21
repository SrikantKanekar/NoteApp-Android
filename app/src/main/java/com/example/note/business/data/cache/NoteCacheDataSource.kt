package com.example.note.business.data.cache

import com.example.note.business.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteCacheDataSource {

    suspend fun insertNote(note: Note): Long

    suspend fun insertNotes(notes: List<Note>): LongArray

    suspend fun updateNote(
        id: String,
        title: String?,
        body: String?,
        update_at: String
    ): Int

    suspend fun getNote(id: String): Note?

    suspend fun getAllNotes(): List<Note>

    suspend fun getNumNotes(): Int

    suspend fun deleteNote(id: String): Int

    suspend fun deleteNotes(notes: List<Note>): Int

    fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<List<Note>>
}

package com.example.note.cache.database.dataSource

import com.example.note.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteCacheDataSource {

    suspend fun insertNotes(notes: List<Note>)

    suspend fun updateNotes(notes: List<Note>)

    suspend fun deleteNotes(ids: List<String>)

    suspend fun getNote(id: String): Note?

    suspend fun getAllNotes(): List<Note>

    fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<List<Note>>
}

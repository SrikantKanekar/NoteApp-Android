package com.example.note.business.data.cache

import com.example.note.business.domain.model.Note
import kotlinx.coroutines.flow.Flow

class NoteCacheRepository(
    private val noteCacheDataSource: NoteCacheDataSource
) : NoteCacheDataSource {

    override suspend fun insertNote(note: Note): Long {
        return noteCacheDataSource.insertNote(note)
    }

    override suspend fun insertNotes(notes: List<Note>): LongArray {
        return noteCacheDataSource.insertNotes(notes)
    }

    override suspend fun updateNote(
        id: String,
        newTitle: String,
        newBody: String?,
        timestamp: String?
    ): Int {
        return noteCacheDataSource.updateNote(id, newTitle, newBody, timestamp)
    }

    override suspend fun getNote(id: String): Note? {
        return noteCacheDataSource.getNote(id)
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteCacheDataSource.getAllNotes()
    }

    override suspend fun deleteNote(id: String): Int {
        return noteCacheDataSource.deleteNote(id)
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
        return noteCacheDataSource.deleteNotes(notes)
    }

    override fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): Flow<List<Note>> {
        return noteCacheDataSource.searchNotes(query, filterAndOrder, page)
    }
}
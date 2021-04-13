package com.example.note.business.data.cache

import com.example.note.business.domain.model.Note

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
        primaryKey: String,
        newTitle: String,
        newBody: String?,
        timestamp: String?
    ): Int {
        return noteCacheDataSource.updateNote(primaryKey, newTitle, newBody, timestamp)
    }

    override suspend fun searchNoteById(id: String): Note? {
        return noteCacheDataSource.searchNoteById(id)
    }

    override suspend fun getNumNotes(): Int {
        return noteCacheDataSource.getNumNotes()
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteCacheDataSource.getAllNotes()
    }

    override suspend fun deleteNote(primaryKey: String): Int {
        return noteCacheDataSource.deleteNote(primaryKey)
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
        return noteCacheDataSource.deleteNotes(notes)
    }

    override suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        return searchNotes(query, filterAndOrder, page)
    }


}
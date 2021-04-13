package com.example.note.business.data.network

import com.example.note.business.domain.model.Note
import com.example.note.framework.datasource.network.response.SimpleResponse

class NoteNetworkRepository(
    private val noteNetworkDataSource: NoteNetworkDataSource
): NoteNetworkDataSource {

    override suspend fun insertOrUpdateNote(note: Note): SimpleResponse {
        return noteNetworkDataSource.insertOrUpdateNote(note)
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): SimpleResponse {
        return noteNetworkDataSource.insertOrUpdateNotes(notes)
    }

    override suspend fun searchNote(note: Note): Note? {
        return noteNetworkDataSource.searchNote(note)
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteNetworkDataSource.getAllNotes()
    }

    override suspend fun deleteNote(id: String): SimpleResponse {
        return noteNetworkDataSource.deleteNote(id)
    }

    override suspend fun deleteAllNotes(): SimpleResponse {
        return noteNetworkDataSource.deleteAllNotes()
    }

    override suspend fun insertDeletedNote(note: Note): SimpleResponse {
        return noteNetworkDataSource.insertDeletedNote(note)
    }

    override suspend fun insertDeletedNotes(notes: List<Note>): SimpleResponse {
        return noteNetworkDataSource.insertDeletedNotes(notes)
    }

    override suspend fun getDeletedNotes(): List<Note> {
        return noteNetworkDataSource.getDeletedNotes()
    }

    override suspend fun deleteDeletedNote(note: Note): SimpleResponse {
        return noteNetworkDataSource.deleteDeletedNote(note)
    }
}
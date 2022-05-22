package com.example.note.business.data.network

import com.example.note.business.domain.model.Note
import com.example.note.framework.datasource.network.response.SimpleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteNetworkRepository @Inject constructor(
    private val noteNetworkDataSource: NoteNetworkDataSource,
    private val deletedNotesNetworkDataSource: DeletedNotesNetworkDataSource
) : NoteNetworkDataSource, DeletedNotesNetworkDataSource {

    override suspend fun insertOrUpdateNote(note: Note): SimpleResponse {
        return noteNetworkDataSource.insertOrUpdateNote(note)
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): SimpleResponse {
        return noteNetworkDataSource.insertOrUpdateNotes(notes)
    }

    override suspend fun getNote(id: String): Note? {
        return noteNetworkDataSource.getNote(id)
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

    override suspend fun insertDeletedNote(id: String): SimpleResponse {
        return deletedNotesNetworkDataSource.insertDeletedNote(id)
    }

    override suspend fun insertDeletedNotes(notes: List<Note>): SimpleResponse {
        return deletedNotesNetworkDataSource.insertDeletedNotes(notes)
    }

    override suspend fun getDeletedNotes(): List<Note> {
        return deletedNotesNetworkDataSource.getDeletedNotes()
    }

    override suspend fun deleteDeletedNote(id: String): SimpleResponse {
        return deletedNotesNetworkDataSource.deleteDeletedNote(id)
    }
}
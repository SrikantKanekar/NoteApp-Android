package com.example.note.business.data.network

import com.example.note.business.domain.model.Note
import com.example.note.framework.datasource.network.response.SimpleResponse

interface NoteNetworkDataSource {

    suspend fun insertOrUpdateNote(note: Note): SimpleResponse

    suspend fun insertOrUpdateNotes(notes: List<Note>): SimpleResponse

    suspend fun searchNote(note: Note): Note?

    suspend fun getAllNotes(): List<Note>

    suspend fun deleteNote(id: String): SimpleResponse

    suspend fun deleteAllNotes(): SimpleResponse

    suspend fun insertDeletedNote(id: String): SimpleResponse

    suspend fun insertDeletedNotes(notes: List<Note>): SimpleResponse

    suspend fun getDeletedNotes(): List<Note>

    suspend fun deleteDeletedNote(note: Note): SimpleResponse
}
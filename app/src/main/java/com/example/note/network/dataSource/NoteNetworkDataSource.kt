package com.example.note.network.dataSource

import com.example.note.model.Note

interface NoteNetworkDataSource {

    suspend fun insertOrUpdateNote(note: Note)

    suspend fun insertOrUpdateNotes(notes: List<Note>)

    suspend fun deleteNote(id: String)

    suspend fun deleteNotes(ids: List<String>)

    suspend fun getNote(id: String): Note?

    suspend fun getAllNotes(): List<Note>
}
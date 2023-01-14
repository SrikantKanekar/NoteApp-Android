package com.example.note.network.dataSource

import com.example.note.model.Note

interface NoteNetworkDataSource {

    suspend fun insertOrUpdateNotes(notes: List<Note>)

    suspend fun deleteNotes(ids: List<String>)

    suspend fun getNote(id: String): Note?

    suspend fun getAllNotes(): List<Note>
}
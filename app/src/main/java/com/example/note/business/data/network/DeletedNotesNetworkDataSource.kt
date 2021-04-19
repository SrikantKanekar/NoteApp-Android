package com.example.note.business.data.network

import com.example.note.business.domain.model.Note
import com.example.note.framework.datasource.network.response.SimpleResponse

interface DeletedNotesNetworkDataSource {

    suspend fun insertDeletedNote(id: String): SimpleResponse

    suspend fun insertDeletedNotes(notes: List<Note>): SimpleResponse

    suspend fun getDeletedNotes(): List<Note>

    suspend fun deleteDeletedNote(id: String): SimpleResponse
}
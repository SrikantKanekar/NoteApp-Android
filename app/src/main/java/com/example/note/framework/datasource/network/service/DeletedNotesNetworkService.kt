package com.example.note.framework.datasource.network.service

import com.example.note.business.data.network.DeletedNotesNetworkDataSource
import com.example.note.business.domain.model.Note
import com.example.note.framework.datasource.network.api.DeletedNotesApi
import com.example.note.framework.datasource.network.mapper.NoteDtoMapper
import com.example.note.framework.datasource.network.response.SimpleResponse

class DeletedNotesNetworkService(
    private val deletedNotesApi: DeletedNotesApi,
    private val noteDtoMapper: NoteDtoMapper
): DeletedNotesNetworkDataSource {

    override suspend fun insertDeletedNote(id: String): SimpleResponse {
        return deletedNotesApi.insertDeletedNote(id)
    }

    override suspend fun insertDeletedNotes(notes: List<Note>): SimpleResponse {
        return deletedNotesApi.insertDeletedNotes(noteDtoMapper.noteListToDtoList(notes))
    }

    override suspend fun getDeletedNotes(): List<Note> {
        return noteDtoMapper.dtoListToNoteList(
            deletedNotesApi.getDeletedNotes()
        )
    }

    override suspend fun deleteDeletedNote(id: String): SimpleResponse {
        return deletedNotesApi.deleteDeletedNote(id)
    }
}
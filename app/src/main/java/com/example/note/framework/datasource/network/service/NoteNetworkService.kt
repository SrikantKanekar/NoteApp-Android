package com.example.note.framework.datasource.network.service

import com.example.note.business.data.network.NoteNetworkDataSource
import com.example.note.business.domain.model.Note
import com.example.note.framework.datasource.network.api.NoteApi
import com.example.note.framework.datasource.network.mapper.NoteDtoMapper
import com.example.note.framework.datasource.network.response.SimpleResponse

class NoteNetworkService(
    private val noteApi: NoteApi,
    private val noteDtoMapper: NoteDtoMapper
): NoteNetworkDataSource {

    override suspend fun insertOrUpdateNote(note: Note): SimpleResponse {
        val noteDto = noteDtoMapper.mapFromDomainModel(note)
        return noteApi.insertOrUpdateNote(noteDto)
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): SimpleResponse {
        return noteApi.insertOrUpdateNotes(noteDtoMapper.noteListToDtoList(notes))
    }

    override suspend fun getNote(id: String): Note? {
        val noteDto = noteApi.getNote(id)
        return when(noteDto == null){
            true -> return null
            false -> noteDtoMapper.mapToDomainModel(noteDto)
        }
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteDtoMapper.dtoListToNoteList(
            noteApi.getAllNotes()
        )
    }

    override suspend fun deleteNote(id: String): SimpleResponse {
        return noteApi.deleteNote(id)
    }

    override suspend fun deleteAllNotes(): SimpleResponse {
        return noteApi.deleteAllNotes()
    }
}
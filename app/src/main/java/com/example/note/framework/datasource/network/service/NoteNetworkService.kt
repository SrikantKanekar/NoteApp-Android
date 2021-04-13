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
        return noteApi.insertOrUpdateNote(noteDtoMapper.mapFromDomainModel(note))
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): SimpleResponse {
        return noteApi.insertOrUpdateNotes(noteDtoMapper.noteListToDtoList(notes))
    }

    override suspend fun searchNote(note: Note): Note? {
        val noteDto = noteApi.searchNote(noteDtoMapper.mapFromDomainModel(note))
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

    override suspend fun insertDeletedNote(note: Note): SimpleResponse {
        return noteApi.insertDeletedNote(noteDtoMapper.mapFromDomainModel(note))
    }

    override suspend fun insertDeletedNotes(notes: List<Note>): SimpleResponse {
        return noteApi.insertDeletedNotes(noteDtoMapper.noteListToDtoList(notes))
    }

    override suspend fun getDeletedNotes(): List<Note> {
        return noteDtoMapper.dtoListToNoteList(
            noteApi.getDeletedNotes()
        )
    }

    override suspend fun deleteDeletedNote(note: Note): SimpleResponse {
        return noteApi.deleteDeletedNote(noteDtoMapper.mapFromDomainModel(note))
    }
}
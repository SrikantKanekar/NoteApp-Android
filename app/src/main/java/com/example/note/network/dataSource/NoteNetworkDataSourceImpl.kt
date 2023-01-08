package com.example.note.network.dataSource

import com.example.note.model.Note
import com.example.note.network.api.NoteApi
import com.example.note.network.mapper.NoteDtoMapper
import com.example.note.network.requests.NoteDeleteRequest
import com.example.note.network.requests.NoteInsertOrUpdateRequest
import com.example.note.util.apiCall
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteNetworkDataSourceImpl @Inject constructor(
    private val noteApi: NoteApi,
    private val mapper: NoteDtoMapper
) : NoteNetworkDataSource {

    override suspend fun insertOrUpdateNotes(notes: List<Note>) {
        when {
            notes.isNotEmpty() -> {
                apiCall(Dispatchers.IO) {
                    noteApi.insertOrUpdateNotes(
                        NoteInsertOrUpdateRequest(
                            notes = notes.map { note ->
                                mapper.fromModel(note)
                            }
                        )
                    )
                }
            }
        }
    }

    override suspend fun deleteNotes(ids: List<String>) {
        when {
            ids.isNotEmpty() -> {
                apiCall(Dispatchers.IO) {
                    noteApi.deleteNotes(
                        NoteDeleteRequest(ids)
                    )
                }
            }
        }
    }

    override suspend fun getNote(id: String): Note? {
        return apiCall(Dispatchers.IO) {
            val noteDto = noteApi.getNote(id)
            when (noteDto == null) {
                false -> mapper.toModel(noteDto)
                true -> null
            }
        }
    }

    override suspend fun getAllNotes(): List<Note> {
        return apiCall(Dispatchers.IO) {
            noteApi.getAllNotes().map { noteDto ->
                mapper.toModel(noteDto)
            }
        } ?: listOf()
    }
}
package com.example.note.network.dataSource

import com.example.note.model.Note
import com.example.note.network.api.NoteApi
import com.example.note.network.mapper.NoteDtoMapper
import com.example.note.util.apiCall
import com.example.note.util.printServerResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteNetworkDataSourceImpl @Inject constructor(
    private val noteApi: NoteApi,
    private val mapper: NoteDtoMapper
) : NoteNetworkDataSource {

    override suspend fun insertOrUpdateNote(note: Note) {
        apiCall(Dispatchers.IO) {
            val networkResponse = noteApi.insertOrUpdateNote(
                mapper.fromModel(note)
            )
            printServerResponse("insertOrUpdateNote", networkResponse)
        }
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>) {
        when {
            notes.isNotEmpty() -> {
                apiCall(Dispatchers.IO) {
                    val networkResponse = noteApi.insertOrUpdateNotes(
                        notes.map { note ->
                            mapper.fromModel(note)
                        }
                    )
                    printServerResponse("insertOrUpdateNotes", networkResponse)
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

    override suspend fun deleteNote(id: String) {
        apiCall(Dispatchers.IO) {
            val networkResponse = noteApi.deleteNote(id)
            printServerResponse("deleteNote", networkResponse)
        }
    }
}
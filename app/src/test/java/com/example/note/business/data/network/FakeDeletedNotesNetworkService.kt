package com.example.note.business.data.network

import com.example.note.business.domain.model.Note
import com.example.note.framework.datasource.network.response.SimpleResponse

class FakeDeletedNotesNetworkService
constructor(
    private val notesData: HashMap<String, Note>,
    private val deletedNotesData: HashMap<String, Note>
) : DeletedNotesNetworkDataSource {

    override suspend fun insertDeletedNote(id: String): SimpleResponse {
        val note = notesData[id]!!
        deletedNotesData[id] = note
        return SimpleResponse(true, "")
    }

    override suspend fun insertDeletedNotes(notes: List<Note>): SimpleResponse {
        for (note in notes) {
            deletedNotesData[note.id] = note
        }
        return SimpleResponse(true, "")
    }

    override suspend fun getDeletedNotes(): List<Note> {
        return ArrayList(deletedNotesData.values)
    }

    override suspend fun deleteDeletedNote(id: String): SimpleResponse {
        deletedNotesData.remove(id)
        return SimpleResponse(true, "")
    }
}

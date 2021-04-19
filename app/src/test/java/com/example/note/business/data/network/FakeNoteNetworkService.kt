package com.example.note.business.data.network

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.util.DateUtil
import com.example.note.framework.datasource.network.response.SimpleResponse

class FakeNoteNetworkService
constructor(
    private val notesData: HashMap<String, Note>,
    private val deletedNotesData: HashMap<String, Note>,
    private val dateUtil: DateUtil
) : NoteNetworkDataSource {

    override suspend fun insertOrUpdateNote(note: Note): SimpleResponse {
        notesData[note.id] = note.copy(updated_at = dateUtil.getCurrentTimestamp())
        return SimpleResponse(true, "")
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): SimpleResponse {
        for (note in notes) {
            notesData[note.id] = note.copy(updated_at = dateUtil.getCurrentTimestamp())
        }
        return SimpleResponse(true, "")
    }

    override suspend fun getNote(id: String): Note? {
        return notesData[id]
    }

    override suspend fun getAllNotes(): List<Note> {
        return ArrayList(notesData.values)
    }

    override suspend fun deleteNote(id: String): SimpleResponse {
        notesData.remove(id)
        return SimpleResponse(true, "")
    }

    override suspend fun deleteAllNotes(): SimpleResponse {
        deletedNotesData.clear()
        return SimpleResponse(true, "")
    }
}

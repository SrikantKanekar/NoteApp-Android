package com.example.note.business.data.network

import com.example.note.business.domain.model.Note
import com.example.note.framework.datasource.network.response.SimpleResponse

class FakeNoteNetworkService
constructor(
    private val notesData: HashMap<String, Note>,
    private val deletedNotesData: HashMap<String, Note>
) : NoteNetworkDataSource {

    override suspend fun insertOrUpdateNote(note: Note): SimpleResponse {
        notesData[note.id] = note
        return SimpleResponse(true, "note inserted or updated")
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): SimpleResponse {
        for (note in notes) {
            notesData[note.id] = note
        }
        return SimpleResponse(true, "notes inserted or updated")
    }

    override suspend fun getNote(id: String): Note? {
        return notesData[id]
    }

    override suspend fun getAllNotes(): List<Note> {
        return ArrayList(notesData.values)
    }

    override suspend fun deleteNote(id: String): SimpleResponse {
        val result = notesData.remove(id)
        return SimpleResponse(true, "deleted note ${result?.title}")
    }

    override suspend fun deleteAllNotes(): SimpleResponse {
        deletedNotesData.clear()
        return SimpleResponse(true, "Deleted all notes")
    }
}

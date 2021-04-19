package com.example.note.business.data.network

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.util.DateUtil
import com.example.note.framework.datasource.network.response.SimpleResponse

class FakeNoteNetworkDataSourceImpl
constructor(
    private val notesData: HashMap<String, Note>,
    private val deletedNotesData: HashMap<String, Note>,
    private val dateUtil: DateUtil
) : NoteNetworkDataSource {

    override suspend fun insertOrUpdateNote(note: Note): SimpleResponse {
        val n = Note(
            id = note.id,
            title = note.title,
            body = note.body,
            created_at = note.created_at,
            updated_at = dateUtil.getCurrentTimestamp()
        )
        notesData[note.id] = n
        return SimpleResponse(true, "")
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): SimpleResponse {
        for (note in notes) {
            notesData[note.id] = note
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

//    override suspend fun insertDeletedNote(note: Note) {
//        deletedNotesData[note.id] = note
//    }
//
//    override suspend fun insertDeletedNotes(notes: List<Note>) {
//        for(note in notes){
//            deletedNotesData[note.id] = note
//        }
//    }
//
//    override suspend fun getDeletedNotes(): List<Note> {
//        return ArrayList(deletedNotesData.values)
//    }
//
//    override suspend fun deleteDeletedNote(note: Note) {
//        deletedNotesData.remove(note.id)
//    }
}

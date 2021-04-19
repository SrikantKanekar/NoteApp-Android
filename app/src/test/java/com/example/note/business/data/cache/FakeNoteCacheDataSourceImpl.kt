package com.example.note.business.data.cache

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.util.DateUtil
import com.example.note.framework.datasource.cache.NOTE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val FORCE_DELETE_NOTE_EXCEPTION = "FORCE_DELETE_NOTE_EXCEPTION"
const val FORCE_DELETES_NOTE_EXCEPTION = "FORCE_DELETES_NOTE_EXCEPTION"
const val FORCE_UPDATE_NOTE_EXCEPTION = "FORCE_UPDATE_NOTE_EXCEPTION"
const val FORCE_NEW_NOTE_EXCEPTION = "FORCE_NEW_NOTE_EXCEPTION"
const val FORCE_SEARCH_NOTES_EXCEPTION = "FORCE_SEARCH_NOTES_EXCEPTION"
const val FORCE_GENERAL_FAILURE = "FORCE_GENERAL_FAILURE"

class FakeNoteCacheDataSourceImpl
constructor(
    private val notesData: HashMap<String, Note>,
    private val dateUtil: DateUtil
) : NoteCacheDataSource {

    override suspend fun insertNote(note: Note): Long {
        if (note.id == FORCE_NEW_NOTE_EXCEPTION) {
            throw Exception("Something went wrong inserting the note.")
        }
        if (note.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        notesData[note.id] = note
        return 1 // success
    }

    override suspend fun insertNotes(notes: List<Note>): LongArray {
        val results = LongArray(notes.size)
        for ((index, note) in notes.withIndex()) {
            results[index] = 1
            notesData[note.id] = note
        }
        return results
    }

    override suspend fun updateNote(
        primaryKey: String,
        newTitle: String,
        newBody: String?,
        timestamp: String?
    ): Int {
        if (primaryKey == FORCE_UPDATE_NOTE_EXCEPTION) {
            throw Exception("Something went wrong updating the note.")
        }
        val updatedNote = Note(
            id = primaryKey,
            title = newTitle,
            body = newBody ?: "",
            updated_at = timestamp ?: dateUtil.getCurrentTimestamp(),
            created_at = notesData[primaryKey]?.created_at ?: dateUtil.getCurrentTimestamp()
        )
        return notesData[primaryKey]?.let {
            notesData[primaryKey] = updatedNote
            1 // success
        } ?: -1 // nothing to update
    }

    override suspend fun getNote(id: String): Note? {
        return notesData[id]
    }

    override suspend fun getAllNotes(): List<Note> {
        return ArrayList(notesData.values)
    }

    override suspend fun deleteNote(id: String): Int {
        if (id == FORCE_DELETE_NOTE_EXCEPTION) {
            throw Exception("Something went wrong deleting the note.")
        } else if (id == FORCE_DELETES_NOTE_EXCEPTION) {
            throw Exception("Something went wrong deleting the note.")
        }
        return notesData.remove(id)?.let {
            1 // return 1 for success
        } ?: -1 // -1 for failure
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
        var failOrSuccess = 1
        for (note in notes) {
            if (notesData.remove(note.id) == null) {
                failOrSuccess = -1 // mark for failure
            }
        }
        return failOrSuccess
    }

    // Not testing the order/filter. Just basic query
    // simulate SQLite "LIKE" query on title and body
    override fun searchNotes(
        query: String,
        filterAndOrder:
        String,
        page: Int
    ): Flow<List<Note>> = flow {
        if (query == FORCE_SEARCH_NOTES_EXCEPTION) {
            throw Exception("Something went searching the cache for notes.")
        }
        val results: ArrayList<Note> = ArrayList()
        for (note in notesData.values) {
            if (note.title.contains(query)) {
                results.add(note)
            } else if (note.body.contains(query)) {
                results.add(note)
            }
            if (results.size > (page * NOTE_PAGINATION_PAGE_SIZE)) {
                break
            }
        }
        emit(results)
    }
}

package com.example.note.business.data.cache

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.util.DateUtil
import com.example.note.framework.datasource.cache.NOTE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val FORCE_CACHE_INSERT_FAILURE = "FORCE_CACHE_INSERT_FAILURE"
const val FORCE_NEW_NOTE_EXCEPTION = "FORCE_NEW_NOTE_EXCEPTION"
const val FORCE_SEARCH_NOTES_EXCEPTION = "FORCE_SEARCH_NOTES_EXCEPTION"
const val FORCE_UPDATE_NOTE_EXCEPTION = "FORCE_UPDATE_NOTE_EXCEPTION"
const val FORCE_DELETE_NOTE_EXCEPTION = "FORCE_DELETE_NOTE_EXCEPTION"
const val FORCE_DELETES_NOTE_EXCEPTION = "FORCE_DELETES_NOTE_EXCEPTION"

class FakeNoteCacheService
constructor(
    private val notesData: HashMap<String, Note>,
    private val dateUtil: DateUtil
) : NoteCacheDataSource {

    override suspend fun insertNote(note: Note): Long {
        return when (note.id) {
            FORCE_NEW_NOTE_EXCEPTION -> {
                throw Exception("error inserting the note into cache")
            }
            FORCE_CACHE_INSERT_FAILURE -> {
                -1 // fail
            }
            else -> {
                notesData[note.id] = note
                1 // success
            }
        }
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
        id: String,
        title: String?,
        body: String?,
        update_at: String
    ): Int {

        when (id) {
            FORCE_UPDATE_NOTE_EXCEPTION -> {
                throw Exception("Something went wrong updating the note")
            }
            else -> return when (notesData[id]) {
                null -> -1 // nothing to update
                else -> {
                    notesData[id] = Note(
                        id = id,
                        title = title ?: notesData[id]?.title ?: "",
                        body = body ?: notesData[id]?.body ?: "",
                        updated_at = update_at,
                        created_at = notesData[id]?.created_at ?: dateUtil.getCurrentTimestamp()
                    )
                    1 // success
                }
            }
        }
    }

    override suspend fun getNote(id: String): Note? {
        return notesData[id]
    }

    override suspend fun getAllNotes(): List<Note> {
        return ArrayList(notesData.values)
    }

    override suspend fun getNumNotes(): Int {
        return notesData.size
    }

    override suspend fun deleteNote(id: String): Int {
        when (id) {
            FORCE_DELETE_NOTE_EXCEPTION -> {
                throw Exception("Something went wrong deleting the note")
            }
            FORCE_DELETES_NOTE_EXCEPTION -> {
                throw Exception("Something went wrong deleting the note")
            }
            else -> return when (notesData.remove(id)) {
                null -> -1 // -1 for failure
                else -> 1 // return 1 for success
            }
        }
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
        filterAndOrder: String,
        page: Int
    ): Flow<List<Note>> = flow {
        if (query == FORCE_SEARCH_NOTES_EXCEPTION) {
            throw Exception("Something went searching the cache for notes")
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
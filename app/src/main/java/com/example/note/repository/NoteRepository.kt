package com.example.note.repository

import com.example.note.model.Note
import com.example.note.util.NOTE_FILTER_DATE_CREATED
import com.example.note.util.NOTE_ORDER_DESC
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun insertNote(note: Note)

    suspend fun insertNotes(notes: List<Note>)

    suspend fun updateNote(note: Note)

    suspend fun updateNotes(notes: List<Note>)

    suspend fun deleteNote(note: Note)

    suspend fun deleteNotes(notes: List<Note>)

    suspend fun getNote(id: String): Note

    suspend fun getAllNotes(): List<Note>

    fun searchNotes(
        query: String? = "",
        filterAndOrder: String = NOTE_ORDER_DESC + NOTE_FILTER_DATE_CREATED,
        page: Int = 1
    ): Flow<List<Note>>

    suspend fun syncNotes()
}

package com.example.note.framework.datasource.cache

import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.util.DateUtil
import kotlinx.coroutines.flow.map

class NoteCacheService(
    private val noteDao: NoteDao,
    private val noteCacheMapper: NoteCacheMapper,
    private val dateUtil: DateUtil
) : NoteCacheDataSource {

    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(noteCacheMapper.mapFromDomainModel(note))
    }

    override suspend fun insertNotes(notes: List<Note>): LongArray {
        return noteDao.insertNotes(
            noteCacheMapper.noteListToEntityList(notes)
        )
    }

    override suspend fun updateNote(
        id: String,
        newTitle: String,
        newBody: String?,
        timestamp: String?
    ): Int {
        return noteDao.updateNote(
            primaryKey = id,
            title = newTitle,
            body = newBody,
            updated_at = timestamp ?: dateUtil.getCurrentTimestamp()
        )
    }

    override suspend fun getNote(id: String): Note? {
        return noteDao.searchNoteById(id)?.let { note ->
            noteCacheMapper.mapToDomainModel(note)
        }
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteCacheMapper.entityListToNoteList(noteDao.getAllNotes())
    }

    override suspend fun deleteNote(id: String): Int {
        return noteDao.deleteNote(id)
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
        val ids = notes.mapIndexed { _, value -> value.id }
        return noteDao.deleteNotes(ids)
    }

    override fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ) = noteDao.searchNotes(query, filterAndOrder, page)
        .map {
            noteCacheMapper.entityListToNoteList(it)
        }
}
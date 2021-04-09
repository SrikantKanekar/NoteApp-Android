package com.example.note.framework.datasource.cache

import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.util.DateUtil

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
        primaryKey: String,
        newTitle: String,
        newBody: String?,
        timestamp: String?
    ): Int {
        return if (timestamp != null) {
            noteDao.updateNote(
                primaryKey = primaryKey,
                title = newTitle,
                body = newBody,
                updated_at = timestamp
            )
        } else {
            noteDao.updateNote(
                primaryKey = primaryKey,
                title = newTitle,
                body = newBody,
                updated_at = dateUtil.getCurrentTimestamp()
            )
        }

    }

    override suspend fun searchNoteById(id: String): Note? {
        return noteDao.searchNoteById(id)?.let { note ->
            noteCacheMapper.mapToDomainModel(note)
        }
    }

    override suspend fun getNumNotes(): Int {
        return noteDao.getNumNotes()
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteCacheMapper.entityListToNoteList(noteDao.getAllNotes())
    }

    override suspend fun deleteNote(primaryKey: String): Int {
        return noteDao.deleteNote(primaryKey)
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
        val ids = notes.mapIndexed { _, value -> value.id }
        return noteDao.deleteNotes(ids)
    }

    override suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        return noteCacheMapper.entityListToNoteList(
            noteDao.searchNotes()
        )
    }

    suspend fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteCacheMapper.entityListToNoteList(
            noteDao.searchNotesOrderByDateDESC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    suspend fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteCacheMapper.entityListToNoteList(
            noteDao.searchNotesOrderByDateASC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    suspend fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteCacheMapper.entityListToNoteList(
            noteDao.searchNotesOrderByTitleDESC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    suspend fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteCacheMapper.entityListToNoteList(
            noteDao.searchNotesOrderByTitleASC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        return noteCacheMapper.entityListToNoteList(
            noteDao.returnOrderedQuery(
                query = query,
                page = page,
                filterAndOrder = filterAndOrder
            )
        )
    }
}

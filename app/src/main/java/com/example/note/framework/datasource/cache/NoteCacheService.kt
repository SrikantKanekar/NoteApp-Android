package com.example.note.framework.datasource.cache

import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.business.domain.model.Note
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteCacheService @Inject constructor(
    private val noteDao: NoteDao,
    private val noteCacheMapper: NoteCacheMapper
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
        title: String?,
        body: String?,
        update_at: String
    ): Int {
        return noteDao.updateNote(
            id = id,
            title = title,
            body = body,
            updated_at = update_at
        )
    }

    override suspend fun getNote(id: String): Note? {
        return noteDao.getNote(id)?.let { note ->
            noteCacheMapper.mapToDomainModel(note)
        }
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteCacheMapper.entityListToNoteList(noteDao.getAllNotes())
    }

    override suspend fun getNumNotes(): Int {
        return noteDao.getNumNotes()
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
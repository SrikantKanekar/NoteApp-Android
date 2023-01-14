package com.example.note.cache.database.dataSource

import com.example.note.cache.database.dao.NoteDao
import com.example.note.cache.database.dao.searchNotes
import com.example.note.cache.database.mapper.NoteEntityMapper
import com.example.note.model.Note
import com.example.note.util.cacheCall
import com.example.note.util.printLogD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteCacheDataSourceImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val mapper: NoteEntityMapper
) : NoteCacheDataSource {

    override suspend fun insertNotes(notes: List<Note>) {
        when {
            notes.isNotEmpty() -> {
                val result = cacheCall(IO) {
                    noteDao.insertNotes(
                        notes.map { note ->
                            mapper.fromModel(note)
                        }
                    )
                }
                printLogD("insertNotes", "${result?.size} notes inserted")
            }
        }
    }

    override suspend fun updateNotes(notes: List<Note>) {
        when {
            notes.isNotEmpty() -> {
                val result = cacheCall(IO) {
                    noteDao.updateNotes(
                        notes.map { note ->
                            mapper.fromModel(note)
                        }
                    )
                }
                printLogD("updateNotes", "$result notes updated")
            }
        }
    }

    override suspend fun deleteNotes(ids: List<String>) {
        when {
            ids.isNotEmpty() -> {
                val result = cacheCall(IO) {
                    noteDao.deleteNotes(ids)
                }
                printLogD("deleteNotes", "$result notes deleted")
            }
        }
    }

    override suspend fun getNote(id: String): Note? {
        return cacheCall(IO) {
            noteDao.getNote(id)?.let { note ->
                mapper.toModel(note)
            }
        }
    }

    override suspend fun getAllNotes(): List<Note> {
        return cacheCall(IO) {
            noteDao.getAllNotes().map { entity ->
                mapper.toModel(entity)
            }
        } ?: listOf()
    }

    override fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ) = noteDao
        .searchNotes(query, filterAndOrder, page)
        .map { entities ->
            entities.map { entity ->
                mapper.toModel(entity)
            }
        }
}
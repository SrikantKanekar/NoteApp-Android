package com.example.note.mock

import com.example.note.cache.database.dao.NoteDao
import com.example.note.cache.database.mapper.NoteEntityMapper
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockSetup @Inject constructor(
    private val noteDao: NoteDao,
    private val mapper: NoteEntityMapper
) {

    fun init() = runBlocking {
        val entities = mockNotes.map { note ->
            mapper.fromModel(note)
        }
        noteDao.insertNotes(entities)
    }
}
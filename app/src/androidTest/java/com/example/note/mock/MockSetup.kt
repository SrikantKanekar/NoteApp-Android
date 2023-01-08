package com.example.note.mock

import com.example.note.cache.database.dao.LabelDao
import com.example.note.cache.database.dao.NoteDao
import com.example.note.cache.database.mapper.LabelEntityMapper
import com.example.note.cache.database.mapper.NoteEntityMapper
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockSetup @Inject constructor(
    private val noteDao: NoteDao,
    private val noteMapper: NoteEntityMapper,
    private val labelDao: LabelDao,
    private val labelMapper: LabelEntityMapper
) {

    fun init() = runBlocking {
        val notes = mockNotes.map { note ->
            noteMapper.fromModel(note)
        }
        val labels = mockLabels.map { label ->
            labelMapper.fromModel(label)
        }
        noteDao.insertNotes(notes)
        labelDao.insertLabels(labels)
    }
}
package com.example.note.di

import com.example.note.business.data.NoteDataFactory
import com.example.note.business.data.cache.FakeNoteCacheService
import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.*
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.util.DateUtil
import com.example.note.business.domain.util.isUnitTest

class DependencyContainer {

    val dateUtil = DateUtil()

    lateinit var noteFactory: NoteFactory
    private lateinit var noteDataFactory: NoteDataFactory

    private lateinit var noteCacheDataSource: NoteCacheDataSource
    lateinit var noteCacheRepository: NoteCacheRepository

    private lateinit var noteNetworkDataSource: NoteNetworkDataSource
    private lateinit var deletedNotesNetworkDataSource: DeletedNotesNetworkDataSource
    lateinit var noteNetworkRepository: NoteNetworkRepository

    init {
        isUnitTest = true
    }

    fun build() {
        this.javaClass.classLoader?.let { classLoader ->
            noteDataFactory = NoteDataFactory(classLoader)
        }
        noteFactory = NoteFactory(dateUtil)

        //Cache
        noteCacheDataSource = FakeNoteCacheService(
            notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            ),
            dateUtil = dateUtil
        )
        noteCacheRepository = NoteCacheRepository(
            noteCacheDataSource = noteCacheDataSource
        )

        //Network
        noteNetworkDataSource = FakeNoteNetworkService(
            notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            ),
            deletedNotesData = HashMap()
        )
        deletedNotesNetworkDataSource = FakeDeletedNotesNetworkService(
            notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            ),
            deletedNotesData = HashMap()
        )
        noteNetworkRepository = NoteNetworkRepository(
            noteNetworkDataSource = noteNetworkDataSource,
            deletedNotesNetworkDataSource = deletedNotesNetworkDataSource
        )
    }
}
package com.example.note.di

import com.example.note.business.data.NoteDataFactory
import com.example.note.business.data.cache.FakeNoteCacheService
import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.*
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.util.DateUtil
import com.example.note.business.domain.util.isUnitTest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DependencyContainer {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val dateUtil = DateUtil(dateFormat)

    lateinit var noteFactory: NoteFactory
    lateinit var noteDataFactory: NoteDataFactory

    lateinit var noteCacheDataSource: NoteCacheDataSource
    lateinit var noteCacheRepository: NoteCacheRepository

    lateinit var noteNetworkDataSource: NoteNetworkDataSource
    lateinit var deletedNotesNetworkDataSource: DeletedNotesNetworkDataSource
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
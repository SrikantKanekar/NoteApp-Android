package com.example.note.di

import com.example.note.business.data.NoteDataFactory
import com.example.note.business.data.cache.FakeNoteCacheService
import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.business.data.network.DeletedNotesNetworkDataSource
import com.example.note.business.data.network.FakeDeletedNotesNetworkService
import com.example.note.business.data.network.FakeNoteNetworkService
import com.example.note.business.data.network.NoteNetworkDataSource
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.util.DateUtil
import com.example.note.business.domain.util.isUnitTest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DependencyContainer {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val dateUtil = DateUtil(dateFormat)
    lateinit var noteNetworkDataSource: NoteNetworkDataSource
    lateinit var deletedNotesNetworkDataSource: DeletedNotesNetworkDataSource
    lateinit var noteCacheDataSource: NoteCacheDataSource
    lateinit var noteFactory: NoteFactory
    lateinit var noteDataFactory: NoteDataFactory

    init {
        isUnitTest = true
    }

    fun build() {
        this.javaClass.classLoader?.let { classLoader ->
            noteDataFactory = NoteDataFactory(classLoader)
        }
        noteFactory = NoteFactory(dateUtil)
        noteNetworkDataSource = FakeNoteNetworkService(
            notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            ),
            deletedNotesData = HashMap(),
            dateUtil = dateUtil
        )
        deletedNotesNetworkDataSource = FakeDeletedNotesNetworkService(
            notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            ),
            deletedNotesData = HashMap()
        )
        noteCacheDataSource = FakeNoteCacheService(
            notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            ),
            dateUtil = dateUtil
        )
    }
}
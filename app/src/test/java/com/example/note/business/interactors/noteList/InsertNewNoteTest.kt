package com.example.note.business.interactors.noteList

import com.example.note.business.data.cache.FORCE_CACHE_INSERT_FAILURE
import com.example.note.business.data.cache.FORCE_NEW_NOTE_EXCEPTION
import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.ERRORS.CACHE_ERROR_UNKNOWN
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.state.MessageType
import com.example.note.business.interactors.notelist.InsertNewNote
import com.example.note.di.DependencyContainer
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

/**
Test cases:
- [insertNote_success_confirmNetworkAndCacheUpdated]
1. insert a new note
2. listen for INSERT_NOTE_SUCCESS emission from flow
3. confirm cache was updated with new note
4. confirm network was updated with new note
- [insertNote_fail_confirmNetworkAndCacheUnchanged]
1. insert a new note
2. force a failure (return -1 from db operation)
3. listen for INSERT_NOTE_FAILED emission from flow
4. confirm cache was not updated
5. confirm network was not updated
- [throwException_checkGenericError_confirmNetworkAndCacheUnchanged]
1. insert a new note
2. force an exception
3. listen for CACHE_ERROR_UNKNOWN emission from flow
4. confirm cache was not updated
5. confirm network was not updated
 **/
@InternalCoroutinesApi
class InsertNewNoteTest {

    // system in test
    private val insertNewNote: InsertNewNote

    // dependencies
    private val dependencyContainer = DependencyContainer()
    private val noteCacheRepository: NoteCacheRepository
    private val noteNetworkRepository: NoteNetworkRepository
    private val noteFactory: NoteFactory

    init {
        dependencyContainer.build()
        noteCacheRepository = dependencyContainer.noteCacheRepository
        noteNetworkRepository = dependencyContainer.noteNetworkRepository
        noteFactory = dependencyContainer.noteFactory
        insertNewNote = InsertNewNote(
            noteCacheRepository = noteCacheRepository,
            noteNetworkRepository = noteNetworkRepository
        )
    }

    @Test
    fun insertNote_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val note = noteFactory.createSingleNote(
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString()
        )

        insertNewNote.execute(
            note = note,
            stateEvent = NoteListStateEvent.InsertNewNoteEvent(note)
        ).collect(object : FlowCollector<DataState<NoteListViewState>?> {
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.messageType,
                    MessageType.Success
                )
            }
        })

        // confirm cache was updated
        val cacheNoteThatWasInserted = noteCacheRepository.getNote(note.id)
        assertTrue { cacheNoteThatWasInserted == note }

        // confirm network was updated
        val networkNoteThatWasInserted = noteNetworkRepository.getNote(note.id)
        assertTrue { networkNoteThatWasInserted == note }
    }

    @Test
    fun insertNote_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

        val newNote = noteFactory.createSingleNote(
            id = FORCE_CACHE_INSERT_FAILURE,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString()
        )

        insertNewNote.execute(
            note = newNote,
            stateEvent = NoteListStateEvent.InsertNewNoteEvent(newNote)
        ).collect(object : FlowCollector<DataState<NoteListViewState>?> {
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.messageType,
                    MessageType.Error
                )
            }
        })

        // confirm cache was not changed
        val cacheNoteThatWasInserted = noteCacheRepository.getNote(newNote.id)
        assertTrue { cacheNoteThatWasInserted == null }

        // confirm network was not changed
        val networkNoteThatWasInserted = noteNetworkRepository.getNote(newNote.id)
        assertTrue { networkNoteThatWasInserted == null }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val newNote = noteFactory.createSingleNote(
            id = FORCE_NEW_NOTE_EXCEPTION,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString()
        )

        insertNewNote.execute(
            note = newNote,
            stateEvent = NoteListStateEvent.InsertNewNoteEvent(newNote)
        ).collect(object : FlowCollector<DataState<NoteListViewState>?> {
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        // confirm cache was not changed
        val cacheNoteThatWasInserted = noteCacheRepository.getNote(newNote.id)
        assertTrue { cacheNoteThatWasInserted == null }

        // confirm network was not changed
        val networkNoteThatWasInserted = noteNetworkRepository.getNote(newNote.id)
        assertTrue { networkNoteThatWasInserted == null }
    }
}



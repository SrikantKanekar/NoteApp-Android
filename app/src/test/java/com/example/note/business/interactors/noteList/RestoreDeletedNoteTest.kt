package com.example.note.business.interactors.noteList

import com.example.note.business.data.cache.FORCE_CACHE_INSERT_FAILURE
import com.example.note.business.data.cache.FORCE_NEW_NOTE_EXCEPTION
import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.ERRORS.CACHE_ERROR_UNKNOWN
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.state.MessageType
import com.example.note.business.interactors.notelist.RestoreDeletedNote
import com.example.note.di.DependencyContainer
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.RestoreDeletedNoteEvent
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
Test cases:
- restoreNote_success_confirmCacheAndNetworkUpdated()
    1. create a new note and insert it into the "deleted" node of network
    2. restore that note
    3. Listen for success msg RESTORE_NOTE_SUCCESS from flow
    4. confirm note is in the cache
    5. confirm note is in the network "notes" node
    6. confirm note is not in the network "deletes" node
 **/
@InternalCoroutinesApi
class RestoreDeletedNoteTest {

    // system in test
    private val restoreDeletedNote: RestoreDeletedNote

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
        restoreDeletedNote = RestoreDeletedNote(
            noteCacheRepository = noteCacheRepository,
            noteNetworkRepository = noteNetworkRepository
        )
    }


    @Test
    fun restoreNote_success_confirmCacheAndNetworkUpdated() =  runBlocking {

        // create a new note and insert into network "deletes" node
        val restoredNote = noteFactory.createSingleNote()

        noteNetworkRepository.insertDeletedNote(restoredNote.id)

        // confirm that note is in the "deletes" node before restoration
        var deletedNotes = noteNetworkRepository.getDeletedNotes()
        assertTrue { deletedNotes.contains(restoredNote) }

        restoreDeletedNote.execute(
            note = restoredNote,
            stateEvent = RestoreDeletedNoteEvent(restoredNote)
        ).collect(object: FlowCollector<DataState<NoteListViewState>?>{
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.messageType,
                    MessageType.Success
                )
            }
        })

        // confirm note is in the cache
        val noteInCache = noteCacheRepository.getNote(restoredNote.id)
        assertTrue { noteInCache == restoredNote }

        // confirm note is in the network "notes" node
        val noteInNetwork = noteNetworkRepository.getNote(restoredNote.id)
        assertTrue { noteInNetwork == restoredNote }

        // confirm note is not in the network "deletes" node
        deletedNotes = noteNetworkRepository.getDeletedNotes()
        assertFalse { deletedNotes.contains(restoredNote) }
    }

    @Test
    fun restoreNote_fail_confirmCacheAndNetworkUnchanged() =  runBlocking {

        // create a new note and insert into network "deletes" node
        val restoredNote = noteFactory.createSingleNote(
            id = FORCE_CACHE_INSERT_FAILURE
        )

        noteNetworkRepository.insertDeletedNote(restoredNote.id)

        // confirm that note is in the "deletes" node before restoration
        var deletedNotes = noteNetworkRepository.getDeletedNotes()
        assertTrue { deletedNotes.contains(restoredNote) }

        restoreDeletedNote.execute(
            note = restoredNote,
            stateEvent = RestoreDeletedNoteEvent(restoredNote)
        ).collect(object: FlowCollector<DataState<NoteListViewState>?>{
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.messageType,
                    MessageType.Error
                )
            }
        })

        // confirm note is not in the cache
        val noteInCache = noteCacheRepository.getNote(restoredNote.id)
        assertTrue { noteInCache == null }

        // confirm note is not in the network "notes" node
        val noteInNetwork = noteNetworkRepository.getNote(restoredNote.id)
        assertTrue { noteInNetwork == null }

        // confirm note is in the network "deletes" node
        deletedNotes = noteNetworkRepository.getDeletedNotes()
        assertTrue { deletedNotes.contains(restoredNote) }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() =  runBlocking {

        // create a new note and insert into network "deletes" node
        val restoredNote = noteFactory.createSingleNote(
            id = FORCE_NEW_NOTE_EXCEPTION,
        )
        noteNetworkRepository.insertDeletedNote(restoredNote.id)

        // confirm that note is in the "deletes" node before restoration
        var deletedNotes = noteNetworkRepository.getDeletedNotes()
        assertTrue { deletedNotes.contains(restoredNote) }

        restoreDeletedNote.execute(
            note = restoredNote,
            stateEvent = RestoreDeletedNoteEvent(restoredNote)
        ).collect(object: FlowCollector<DataState<NoteListViewState>?>{
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        // confirm note is not in the cache
        val noteInCache = noteCacheRepository.getNote(restoredNote.id)
        assertTrue { noteInCache == null }

        // confirm note is not in the network "notes" node
        val noteInNetwork = noteNetworkRepository.getNote(restoredNote.id)
        assertTrue { noteInNetwork == null }

        // confirm note is in the network "deletes" node
        deletedNotes = noteNetworkRepository.getDeletedNotes()
        assertTrue { deletedNotes.contains(restoredNote) }
    }
}
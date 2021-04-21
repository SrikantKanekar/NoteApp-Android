package com.example.note.business.interactors.common

import com.example.note.business.data.cache.FORCE_DELETE_NOTE_EXCEPTION
import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.ERRORS.CACHE_ERROR_UNKNOWN
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.state.MessageType
import com.example.note.di.DependencyContainer
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.DeleteNoteEvent
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

/**
Test cases:
- [deleteNote_success_confirmNetworkUpdated]
1. delete a note
2. check for success message from flow emission
3. confirm note was deleted from "notes" node in network
4. confirm note was added to "deletes" node in network
- [deleteNote_fail_confirmNetworkUnchanged]
1. attempt to delete a note, fail since does not exist
2. check for failure message from flow emission
3. confirm network was not changed
- [throwException_checkGenericError_confirmNetworkUnchanged]
1. attempt to delete a note, force an exception to throw
2. check for failure message from flow emission
3. confirm network was not changed
 **/
@InternalCoroutinesApi
class DeleteNoteTest {

    // system in test
    private val deleteNotes: DeleteNote<NoteListViewState>

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
        deleteNotes = DeleteNote(
            noteCacheRepository = noteCacheRepository,
            noteNetworkRepository = noteNetworkRepository
        )
    }

    @Test
    fun deleteNote_success_confirmNetworkUpdated() = runBlocking {

        // choose a note at random to delete
        val note = noteCacheRepository
            .searchNotes("", "", 1)
            .first()[0]

        deleteNotes.execute(
            note.id,
            DeleteNoteEvent(note.id)
        ).collect(object : FlowCollector<DataState<NoteListViewState>?> {
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.messageType,
                    MessageType.Success
                )
            }
        })

        // confirm was inserted into "deletes" node
        val wasDeletedNoteInserted = noteNetworkRepository.getDeletedNotes().contains(note)
        assertTrue { wasDeletedNoteInserted }

        // confirm was deleted from "notes" node
        val wasNoteDeleted = !noteNetworkRepository.getAllNotes().contains(note)
        assertTrue { wasNoteDeleted }
    }

    @Test
    fun deleteNote_fail_confirmNetworkUnchanged() = runBlocking {

        // create a note to delete that doesn't exist in data set
        val note = noteFactory.createSingleNote()

        deleteNotes.execute(
            note.id,
            DeleteNoteEvent(note.id)
        ).collect(object : FlowCollector<DataState<NoteListViewState>?> {
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.messageType,
                    MessageType.Error
                )
            }
        })

        // confirm was NOT inserted into "deletes" node
        val wasDeletedNoteInserted = !noteNetworkRepository.getDeletedNotes().contains(note)
        assertTrue { wasDeletedNoteInserted }

        // confirm nothing was deleted from "notes" node
        val notes = noteNetworkRepository.getAllNotes()
        val numNotesInCache = noteCacheRepository.getNumNotes()
        assertTrue { numNotesInCache == notes.size }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkUnchanged() = runBlocking {

        // create a note to delete that will throw exception
        val note = noteFactory.createSingleNote(
            id = FORCE_DELETE_NOTE_EXCEPTION,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString()
        )

        deleteNotes.execute(
            note.id,
            DeleteNoteEvent(note.id)
        ).collect(object : FlowCollector<DataState<NoteListViewState>?> {
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        // confirm was NOT inserted into "deletes" node
        val wasDeletedNoteInserted = !noteNetworkRepository.getDeletedNotes().contains(note)
        assertTrue { wasDeletedNoteInserted }

        // confirm nothing was deleted from "notes" node
        val notes = noteNetworkRepository.getAllNotes()
        val numNotesInCache = noteCacheRepository.getNumNotes()
        assertTrue { numNotesInCache == notes.size }
    }
}
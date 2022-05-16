package com.example.note.business.interactors.noteDetail

import com.example.note.business.data.cache.FORCE_UPDATE_NOTE_EXCEPTION
import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.ERRORS.CACHE_ERROR_UNKNOWN
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.state.MessageType
import com.example.note.business.interactors.notedetail.UpdateNote
import com.example.note.di.DependencyContainer
import com.example.note.framework.presentation.ui.noteDetail.state.NoteDetailStateEvent.UpdateNoteEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

/**
Test cases:
- [updateNote_success_confirmNetworkAndCacheUpdated]
1. select a random note from the cache
2. update that note
3. confirm UPDATE_NOTE_SUCCESS msg is emitted from flow
4. confirm note is updated in network
5. confirm note is updated in cache
- [updateNote_fail_confirmNetworkAndCacheUnchanged]
1. attempt to update a note, fail since does not exist
2. check for failure message from flow emission
3. confirm nothing was updated in the cache
- [throwException_checkGenericError_confirmNetworkAndCacheUnchanged]
1. attempt to update a note, force an exception to throw
2. check for failure message from flow emission
3. confirm nothing was updated in the cache
 **/
@InternalCoroutinesApi
class UpdateNoteTest {

    // system in test
    private val updateNote: UpdateNote

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
        updateNote = UpdateNote(
            noteCacheRepository = noteCacheRepository,
            noteNetworkRepository = noteNetworkRepository,
            dateUtil = dependencyContainer.dateUtil
        )
    }

    @Test
    fun updateNote_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val randomNote = noteCacheRepository
            .searchNotes("", "", 1)
            .first()[0]

        val updatedNote = Note(
            id = randomNote.id,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            updated_at = randomNote.updated_at,
            created_at = randomNote.created_at
        )

        updateNote.execute(
            note = updatedNote,
            stateEvent = UpdateNoteEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.messageType,
                MessageType.Success
            )
        }

        // confirm cache was updated
        val cacheNote = noteCacheRepository.getNote(updatedNote.id)
        assertTrue { cacheNote == updatedNote }

        // confirm that network was updated
        val networkNote = noteNetworkRepository.getNote(updatedNote.id)
        assertTrue { networkNote == updatedNote }
    }

    @Test
    fun updateNote_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

        // create a note that doesn't exist in cache
        val noteToUpdate = Note(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            updated_at = UUID.randomUUID().toString(),
            created_at = UUID.randomUUID().toString()
        )

        updateNote.execute(
            note = noteToUpdate,
            stateEvent = UpdateNoteEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.messageType,
                MessageType.Error
            )
        }

        // confirm nothing updated in cache
        val cacheNote = noteCacheRepository.getNote(noteToUpdate.id)
        assertTrue { cacheNote == null }

        // confirm nothing updated in network
        val networkNote = noteNetworkRepository.getNote(noteToUpdate.id)
        assertTrue { networkNote == null }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        // create a note that doesn't exist in cache
        val noteToUpdate = Note(
            id = FORCE_UPDATE_NOTE_EXCEPTION,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            updated_at = UUID.randomUUID().toString(),
            created_at = UUID.randomUUID().toString()
        )

        updateNote.execute(
            note = noteToUpdate,
            stateEvent = UpdateNoteEvent
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CACHE_ERROR_UNKNOWN) ?: false
            )
        }

        // confirm nothing updated in cache
        val cacheNote = noteCacheRepository.getNote(noteToUpdate.id)
        assertTrue { cacheNote == null }

        // confirm nothing updated in network
        val networkNote = noteNetworkRepository.getNote(noteToUpdate.id)
        assertTrue { networkNote == null }
    }
}
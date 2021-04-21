package com.example.note.business.interactors.noteList

import com.example.note.business.data.cache.FORCE_DELETE_NOTE_EXCEPTION
import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.state.MessageType
import com.example.note.business.interactors.notelist.DeleteMultipleNotes
import com.example.note.di.DependencyContainer
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.DeleteMultipleNotesEvent
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

/**
Test cases:
- [deleteNotes_success_confirmNetworkAndCacheUpdated]
1. select a handful of random notes for deleting
2. delete from cache and network
3. confirm DELETE_NOTES_SUCCESS msg is emitted from flow
4. confirm notes are deleted from cache
5. confirm notes are deleted from "notes" node in network
6. confirm notes are added to "deletes" node in network
- [deleteNotes_fail_confirmCorrectDeletesMade]
1. This is a complex one: The use-case will attempt to delete all notes passed as input.
If there is an error with a particular delete, it continues with the others. But the
resulting msg is DELETE_NOTES_ERRORS. So we need to do rigorous checks here
to make sure the correct notes were deleted and the correct notes were not.
2. select a handful of random notes for deleting
3. change the ids of a few notes so they will cause errors when deleting
4. confirm DELETE_NOTES_ERRORS msg is emitted from flow
5. confirm ONLY the valid notes are deleted from network "notes" node
6. confirm ONLY the valid notes are inserted into network "deletes" node
7. confirm ONLY the valid notes are deleted from cache
- [throwException_checkGenericError_confirmNetworkAndCacheUnchanged]
1. select a handful of random notes for deleting
2. force an exception to be thrown on one of them
3. confirm DELETE_NOTES_ERRORS msg is emitted from flow
4. confirm ONLY the valid notes are deleted from network "notes" node
5. confirm ONLY the valid notes are inserted into network "deletes" node
6. confirm ONLY the valid notes are deleted from cache
 **/
@InternalCoroutinesApi
class DeleteMultipleNotesTest {

    // system in test
    private var deleteMultipleNotes: DeleteMultipleNotes? = null

    // dependencies
    private lateinit var dependencyContainer: DependencyContainer
    private lateinit var noteCacheRepository: NoteCacheRepository
    private lateinit var noteNetworkRepository: NoteNetworkRepository
    private lateinit var noteFactory: NoteFactory

    @AfterEach
    fun afterEach() {
        deleteMultipleNotes = null
    }

    @BeforeEach
    fun beforeEach() {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        noteCacheRepository = dependencyContainer.noteCacheRepository
        noteNetworkRepository = dependencyContainer.noteNetworkRepository
        noteFactory = dependencyContainer.noteFactory
        deleteMultipleNotes = DeleteMultipleNotes(
            noteCacheRepository = noteCacheRepository,
            noteNetworkRepository = noteNetworkRepository
        )
    }

    @Test
    fun deleteNotes_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val randomNotes: ArrayList<Note> = ArrayList()
        val notesInCache = noteCacheRepository.searchNotes("", "", 1).first()

        for (note in notesInCache) {
            randomNotes.add(note)
            if (randomNotes.size > 10) {
                break
            }
        }

        deleteMultipleNotes?.execute(
            notes = randomNotes,
            stateEvent = DeleteMultipleNotesEvent(randomNotes)
        )?.collect(object : FlowCollector<DataState<NoteListViewState>?> {
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.messageType,
                    MessageType.Success
                )
            }
        })

        // confirm notes are deleted from cache
        for (note in randomNotes) {
            val noteInCache = noteCacheRepository.getNote(note.id)
            assertTrue { noteInCache == null }
        }

        // confirm notes are added to "deletes" node in network
        val deletedNetworkNotes = noteNetworkRepository.getDeletedNotes()
        assertTrue { deletedNetworkNotes.containsAll(randomNotes) }

        // confirm notes are deleted from "notes" node in network
        val doNotesExistInNetwork = noteNetworkRepository.getAllNotes().containsAll(randomNotes)
        assertFalse { doNotesExistInNetwork }
    }


    @Test
    fun deleteNotes_fail_confirmCorrectDeletesMade() = runBlocking {

        val validNotes: ArrayList<Note> = ArrayList()
        val invalidNotes: ArrayList<Note> = ArrayList()
        val notesInCache = noteCacheRepository.searchNotes("", "", 1).first()

        for ((index, note) in notesInCache.withIndex()) {
            when {
                // try to delete notes which does not exist in notes node
                index % 2 == 0 -> invalidNotes.add(note.copy(id = UUID.randomUUID().toString()))
                else -> validNotes.add(note)
            }
            if ((invalidNotes.size + validNotes.size) > 10) {
                break
            }
        }

        val notesToDelete = ArrayList(validNotes + invalidNotes)
        deleteMultipleNotes?.execute(
            notes = notesToDelete,
            stateEvent = DeleteMultipleNotesEvent(notesToDelete)
        )?.collect(object : FlowCollector<DataState<NoteListViewState>?> {
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.messageType,
                    MessageType.Error
                )
            }
        })


        // confirm ONLY the valid notes are deleted from network "notes" node
        val networkNotes = noteNetworkRepository.getAllNotes()
        assertFalse { networkNotes.containsAll(validNotes) }

        // confirm ONLY the valid notes are inserted into network "deletes" node
        val deletedNetworkNotes = noteNetworkRepository.getDeletedNotes()
        assertTrue { deletedNetworkNotes.containsAll(validNotes) }
        assertFalse { deletedNetworkNotes.containsAll(invalidNotes) }

        // confirm ONLY the valid notes are deleted from cache
        for (note in validNotes) {
            val noteInCache = noteCacheRepository.getNote(note.id)
            assertTrue { noteInCache == null }
        }
        val numNotesInCache = noteCacheRepository.getNumNotes()
        assertTrue { numNotesInCache == (notesInCache.size - validNotes.size) }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val validNotes: ArrayList<Note> = ArrayList()
        val invalidNotes: ArrayList<Note> = ArrayList()
        val notesInCache = noteCacheRepository.searchNotes("", "", 1).first()
        for (note in notesInCache) {
            validNotes.add(note)
            if (validNotes.size > 10) {
                break
            }
        }

        val errorNote = noteFactory.createSingleNote(
            id = FORCE_DELETE_NOTE_EXCEPTION
        )
        invalidNotes.add(errorNote)

        val notesToDelete = ArrayList(validNotes + invalidNotes)
        deleteMultipleNotes?.execute(
            notes = notesToDelete,
            stateEvent = DeleteMultipleNotesEvent(notesToDelete)
        )?.collect(object : FlowCollector<DataState<NoteListViewState>?> {
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.messageType,
                    MessageType.Error
                )
            }
        })


        // confirm ONLY the valid notes are deleted from network "notes" node
        val networkNotes = noteNetworkRepository.getAllNotes()
        assertFalse { networkNotes.containsAll(validNotes) }

        // confirm ONLY the valid notes are inserted into network "deletes" node
        val deletedNetworkNotes = noteNetworkRepository.getDeletedNotes()
        assertTrue { deletedNetworkNotes.containsAll(validNotes) }
        assertFalse { deletedNetworkNotes.containsAll(invalidNotes) }


        // confirm ONLY the valid notes are deleted from cache
        for (note in validNotes) {
            val noteInCache = noteCacheRepository.getNote(note.id)
            assertTrue { noteInCache == null }
        }
        val numNotesInCache = noteCacheRepository.getNumNotes()
        assertTrue { numNotesInCache == (notesInCache.size - validNotes.size) }
    }
}
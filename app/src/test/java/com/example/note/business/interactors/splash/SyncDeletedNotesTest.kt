package com.example.note.business.interactors.splash

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.util.DateUtil
import com.example.note.di.DependencyContainer
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
Test cases:
- [deleteNetworkNotes_confirmCacheSync]
    1. select some notes for deleting from network
    2. delete from network
    3. perform sync
    4. confirm notes from cache were deleted
 **/

@InternalCoroutinesApi
class SyncDeletedNotesTest {

    // system in test
    private val syncDeletedNotes: SyncDeletedNotes

    // dependencies
    private val dependencyContainer = DependencyContainer()
    private val noteCacheRepository: NoteCacheRepository
    private val noteNetworkRepository: NoteNetworkRepository
    private val noteFactory: NoteFactory
    private val dateUtil: DateUtil

    init {
        dependencyContainer.build()
        noteCacheRepository = dependencyContainer.noteCacheRepository
        noteNetworkRepository = dependencyContainer.noteNetworkRepository
        noteFactory = dependencyContainer.noteFactory
        dateUtil = dependencyContainer.dateUtil
        syncDeletedNotes = SyncDeletedNotes(
            noteCacheRepository = noteCacheRepository,
            noteNetworkRepository = noteNetworkRepository
        )
    }

    @Test
    fun deleteNetworkNotes_confirmCacheSync() = runBlocking {

        // select some notes to be deleted from cache
        val networkNotes = noteNetworkRepository.getAllNotes()
        val notesToDelete: ArrayList<Note> = ArrayList()
        for(note in networkNotes){
            notesToDelete.add(note)
            noteNetworkRepository.deleteNote(note.id)
            noteNetworkRepository.insertDeletedNote(note.id)
            if(notesToDelete.size > 6){
                break
            }
        }

        // perform sync
        syncDeletedNotes.syncDeletedNotes()

        // confirm notes were deleted from cache
        for(note in notesToDelete){
            val cachedNote = noteCacheRepository.getNote(note.id)
            assertTrue { cachedNote == null }
        }
    }
}
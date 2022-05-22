package com.example.note.business.interactors.splash

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.util.DateUtil
import com.example.note.di.DependencyContainer
import com.example.note.framework.datasource.cache.ORDER_BY_ASC_DATE_UPDATED
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

/**
Test cases:
- [doSuccessiveUpdatesOccur]
1. update a single network note with new timestamp(updated_at)
2. perform the sync two times with delay of 100 millisecond
3. check to see that the timestamp was not updated a second time
- [checkUpdatedAtDates]
1. update a single network note with new timestamp(updated_at)
2. perform the sync
3. check to see that only a single 'updated_at' date was updated
- [insertNetworkNotesIntoCache]
1. insert a bunch of new notes into the network
2. perform the sync
3. check to see that those notes were inserted into the cache
- [insertCachedNotesIntoNetwork]
1. insert a bunch of new notes into the cache
2. perform the sync
3. check to see that those notes were inserted into the network
- [checkCacheUpdateLogicSync]
1. select some notes from the cache and update them
2. perform sync
3. confirm network reflects the updates
- [checkNetworkUpdateLogicSync]
1. select some notes from the network and update them
2. perform sync
3. confirm cache reflects the updates
 **/

@InternalCoroutinesApi
class SyncNotesTest {

    // system in test
    private val syncNotes: SyncNotes

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
        syncNotes = SyncNotes(
            noteCacheRepository = noteCacheRepository,
            noteNetworkRepository = noteNetworkRepository
        )
    }

    @Test
    fun doSuccessiveUpdatesOccur() = runBlocking {

        // update a single note with new timestamp
        val newDate = dateUtil.getCurrentTimestamp()

        val updatedNote = noteNetworkRepository
            .getAllNotes()[0]
            .copy(updated_at = newDate)

        noteNetworkRepository.insertOrUpdateNote(updatedNote)

        syncNotes.syncNotes()

        delay(1000)

        // simulate launch app again
        syncNotes.syncNotes()

        // confirm the date was not updated a second time
        val notes = noteNetworkRepository.getAllNotes()
        for (note in notes) {
            if (note.id == updatedNote.id) {
                assertTrue { note.updated_at == newDate }
            }
        }
    }

    @Test
    fun checkUpdatedAtDates() = runBlocking {

        // update a single note with new timestamp
        val newDate = dateUtil.getCurrentTimestamp()

        val updatedNote = noteNetworkRepository
            .getAllNotes()[0]
            .copy(updated_at = newDate)

        noteNetworkRepository.insertOrUpdateNote(updatedNote)

        syncNotes.syncNotes()

        // confirm only a single 'updated_at' date was updated
        val notes = noteNetworkRepository.getAllNotes()
        for (note in notes) {
            noteCacheRepository.getNote(note.id)?.let { n ->
                println("date: ${n.updated_at}")
                if (n.id == updatedNote.id) {
                    assertTrue { n.updated_at == newDate }
                } else {
                    assertFalse { n.updated_at == newDate }
                }
            }
        }
    }

    @Test
    fun insertNetworkNotesIntoCache() = runBlocking {

        // prepare the scenario
        // -> Notes in network are newer so they must be inserted into cache
        val newNotes = noteFactory.createNoteList(50)
        noteNetworkRepository.insertOrUpdateNotes(newNotes)

        // perform the sync
        syncNotes.syncNotes()

        // confirm the new notes were inserted into cache
        for (note in newNotes) {
            val cachedNote = noteCacheRepository.getNote(note.id)
            assertTrue { cachedNote != null }
        }
    }


    @Test
    fun insertCachedNotesIntoNetwork() = runBlocking {

        // prepare the scenario
        // -> Notes in cache are newer so they must be inserted into network
        val newNotes = noteFactory.createNoteList(50)
        noteCacheRepository.insertNotes(newNotes)

        // perform the sync
        syncNotes.syncNotes()

        // confirm the new notes were inserted into network
        for (note in newNotes) {
            val networkNote = noteNetworkRepository.getNote(note.id)
            assertTrue { networkNote != null }
        }
    }

    @Test
    fun checkCacheUpdateLogicSync() = runBlocking {

        // select a few notes from cache and update the title and body
        val cachedNotes = noteCacheRepository.searchNotes(
            query = "",
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED,
            page = 1
        ).first()

        val notesToUpdate: ArrayList<Note> = ArrayList()
        for (note in cachedNotes) {
            val updatedNote = note.copy(
                title = UUID.randomUUID().toString(),
                body = UUID.randomUUID().toString(),
                updated_at = dateUtil.getCurrentTimestamp()
            )
            notesToUpdate.add(updatedNote)
            if (notesToUpdate.size > 8) {
                break
            }
        }
        noteCacheRepository.insertNotes(notesToUpdate)

        // perform sync
        syncNotes.syncNotes()

        // confirm the updated notes were updated in the network
        for (note in notesToUpdate) {
            val networkNote = noteNetworkRepository.getNote(note.id)
            assertEquals(note.id, networkNote?.id)
            assertEquals(note.title, networkNote?.title)
            assertEquals(note.body, networkNote?.body)
            assertEquals(note.updated_at, networkNote?.updated_at)
        }
    }

    @Test
    fun checkNetworkUpdateLogicSync() = runBlocking {

        // select a few notes from network and update the title and body
        val networkNotes = noteNetworkRepository.getAllNotes()

        val notesToUpdate: ArrayList<Note> = ArrayList()
        for (note in networkNotes) {
            val updatedNote = note.copy(
                title = UUID.randomUUID().toString(),
                body = UUID.randomUUID().toString(),
                updated_at = dateUtil.getCurrentTimestamp()
            )
            notesToUpdate.add(updatedNote)
            if (notesToUpdate.size > 8) {
                break
            }
        }
        noteNetworkRepository.insertOrUpdateNotes(notesToUpdate)

        // perform sync
        syncNotes.syncNotes()

        // confirm the updated notes were updated in the cache
        for (note in notesToUpdate) {
            val cacheNote = noteCacheRepository.getNote(note.id)
            assertEquals(note.id, cacheNote?.id)
            assertEquals(note.title, cacheNote?.title)
            assertEquals(note.body, cacheNote?.body)
            assertEquals(note.updated_at, cacheNote?.updated_at)
        }
    }
}
package com.example.note.framework.datasource.cache

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.util.DateUtil
import com.example.note.framework.datasource.data.NoteDataFactory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
    CBS = "Confirm by searching"

    Test cases:
    1. confirm database note empty to start (should be test data inserted from CacheTest.kt)
    2. insert a new note, CBS
    3. insert a list of notes, CBS
    4. insert 1000 new notes, confirm filtered search query works correctly
    5. insert 1000 new notes, confirm db size increased
    6. delete new note, confirm deleted
    7. delete list of notes, CBS
    8. update a note, confirm updated
    9. search notes, order by date (ASC), confirm order
    10. search notes, order by date (DESC), confirm order
    11. search notes, order by title (ASC), confirm order
    12. search notes, order by title (DESC), confirm order
 **/

@HiltAndroidTest
class NoteCacheTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // system in test
    @Inject
    lateinit var noteCacheRepository: NoteCacheRepository

    // dependencies
    @Inject
    lateinit var noteDao: NoteDao

    @Inject
    lateinit var dateUtil: DateUtil

    @Inject
    lateinit var cacheMapper: NoteCacheMapper

    @Inject
    lateinit var noteFactory: NoteFactory

    @Inject
    lateinit var noteDataFactory: NoteDataFactory

    @Before
    fun init() {
        hiltRule.inject()
        insertTestData()
    }

    private fun insertTestData() = runBlocking {
        val entityList = cacheMapper.noteListToEntityList(
            noteDataFactory.produceListOfNotes()
        )
        noteDao.insertNotes(entityList)
    }

    /**
     * This test runs first. Check to make sure the test data was inserted from
     * CacheTest class.
     */
    @Test
    fun a_searchNotes_confirmDbNotEmpty() = runBlocking {
        val numNotes = noteCacheRepository.getNumNotes()
        assertTrue { numNotes > 0 }
    }

    @Test
    fun insertNote_CBS() = runBlocking {

        val newNote = noteFactory.createSingleNote()
        noteCacheRepository.insertNote(newNote)

        val cacheNotes = noteCacheRepository.getAllNotes()
        assert(cacheNotes.contains(newNote))
    }

    @Test
    fun insertNoteList_CBS() = runBlocking {

        val noteList = noteFactory.createNoteList(10)
        noteCacheRepository.insertNotes(noteList)

        val cacheNotes = noteCacheRepository.getAllNotes()
        assertTrue { cacheNotes.containsAll(noteList) }
    }

    @Test
    fun insert1000Notes_confirmNumNotesInDb() = runBlocking {
        val currentNumNotes = noteCacheRepository.getNumNotes()

        // insert 1000 notes
        val noteList = noteFactory.createNoteList(1000)
        noteCacheRepository.insertNotes(noteList)

        val numNotes = noteCacheRepository.getNumNotes()
        assertEquals(currentNumNotes + 1000, numNotes)
    }

    @Test
    fun insert1000Notes_searchNotesByTitle_confirm50ExpectedValues() = runBlocking {

        // insert 1000 notes
        val noteList = noteFactory.createNoteList(1000)
        noteCacheRepository.insertNotes(noteList)

        // query 50 notes by specific title
        repeat(50) {
            val randomIndex = Random.nextInt(0, noteList.size - 1)
            val result = noteCacheRepository.searchNotes(
                query = noteList[randomIndex].title,
                page = 1,
                filterAndOrder = ORDER_BY_ASC_TITLE
            ).first()
            assertEquals(noteList[randomIndex].title, result[0].title)
        }
    }


    @Test
    fun insertNote_deleteNote_confirmDeleted() = runBlocking {
        val newNote = noteFactory.createSingleNote()
        noteCacheRepository.insertNote(newNote)

        var notes = noteCacheRepository.getAllNotes()
        assert(notes.contains(newNote))

        noteCacheRepository.deleteNote(newNote.id)
        notes = noteCacheRepository.getAllNotes()
        assert(!notes.contains(newNote))
    }

    @Test
    fun deleteNoteList_confirmDeleted() = runBlocking {
        val noteList: ArrayList<Note> = ArrayList(noteCacheRepository.getAllNotes())

        // select some random notes for deleting
        val notesToDelete: ArrayList<Note> = ArrayList()

        // 1st
        var noteToDelete = noteList[Random.nextInt(0, noteList.size - 1) + 1]
        noteList.remove(noteToDelete)
        notesToDelete.add(noteToDelete)

        // 2nd
        noteToDelete = noteList[Random.nextInt(0, noteList.size - 1) + 1]
        noteList.remove(noteToDelete)
        notesToDelete.add(noteToDelete)

        // 3rd
        noteToDelete = noteList[Random.nextInt(0, noteList.size - 1) + 1]
        noteList.remove(noteToDelete)
        notesToDelete.add(noteToDelete)

        // 4th
        noteToDelete = noteList[Random.nextInt(0, noteList.size - 1) + 1]
        noteList.remove(noteToDelete)
        notesToDelete.add(noteToDelete)

        noteCacheRepository.deleteNotes(notesToDelete)

        // confirm they were deleted
        val searchResults = noteCacheRepository.getAllNotes()
        assertFalse { searchResults.containsAll(notesToDelete) }
    }

    @Test
    fun insertNote_updateNote_confirmUpdated() = runBlocking {
        val newNote = noteFactory.createSingleNote()
        noteCacheRepository.insertNote(newNote)

        // so update timestamp will be different
        delay(1001)

        val newTitle = UUID.randomUUID().toString()
        val newBody = UUID.randomUUID().toString()
        noteCacheRepository.updateNote(
            id = newNote.id,
            title = newTitle,
            body = newBody,
            update_at = dateUtil.getCurrentTimestamp()
        )

        val notes = noteCacheRepository.getAllNotes()

        var foundNote = false
        for (note in notes) {
            if (note.id == newNote.id) {
                foundNote = true
                assertEquals(newNote.id, note.id)
                assertEquals(newTitle, note.title)
                assertEquals(newBody, note.body)
                assert(newNote.updated_at != note.updated_at)
                assertEquals(newNote.created_at, note.created_at)
                break
            }
        }
        assertTrue { foundNote }
    }

    @Test
    fun searchNotes_orderByDateASC_confirmOrder() = runBlocking {
        val noteList = noteCacheRepository.searchNotes(
            query = "",
            page = 1,
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED
        ).first()

        // check that the date gets larger (newer) as iterate down the list
        var previousNoteDate = noteList[0].created_at
        for (index in 1 until noteList.size) {
            val currentNoteDate = noteList[index].created_at
            assertTrue { currentNoteDate >= previousNoteDate }
            previousNoteDate = currentNoteDate
        }
    }

    @Test
    fun searchNotes_orderByDateDESC_confirmOrder() = runBlocking {
        val noteList = noteCacheRepository.searchNotes(
            query = "",
            page = 1,
            filterAndOrder = ORDER_BY_DESC_DATE_UPDATED
        ).first()

        // check that the date gets smaller (older) as iterate down the list
        var previous = noteList[0].created_at
        for (index in 1 until noteList.size) {
            val current = noteList[index].created_at
            assertTrue { current <= previous }
            previous = current
        }
    }

    @Test
    fun searchNotes_orderByTitleASC_confirmOrder() = runBlocking {
        val noteList = noteCacheRepository.searchNotes(
            query = "",
            page = 1,
            filterAndOrder = ORDER_BY_ASC_TITLE
        ).first()

        // check that the title gets larger (newer) as iterate down the list
        var previous = noteList[0].title
        for (index in 1 until noteList.size) {
            val current = noteList[index].title

            assertTrue {
                listOf(previous, current)
                    .asSequence()
                    .zipWithNext { a, b ->
                        a <= b
                    }.all { it }
            }
            previous = current
        }
    }

    @Test
    fun searchNotes_orderByTitleDESC_confirmOrder() = runBlocking {
        val noteList = noteCacheRepository.searchNotes(
            query = "",
            page = 1,
            filterAndOrder = ORDER_BY_DESC_TITLE
        ).first()

        // check that the title gets smaller (older) as iterate down the list
        var previous = noteList[0].title
        for (index in 1 until noteList.size) {
            val current = noteList[index].title

            assertTrue {
                listOf(previous, current)
                    .asSequence()
                    .zipWithNext { a, b ->
                        a >= b
                    }.all { it }
            }
            previous = current
        }
    }
}
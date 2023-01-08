package com.example.note.cache.database

import com.example.note.cache.database.dataSource.NoteCacheDataSource
import com.example.note.mock.MockSetup
import com.example.note.model.Note
import com.example.note.util.*
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
class NoteDatabaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // system in test
    @Inject
    lateinit var noteCacheDataSource: NoteCacheDataSource

    // dependencies
    @Inject
    lateinit var mockSetup: MockSetup

    @Inject
    lateinit var dateUtil: DateUtil

    @Inject
    lateinit var noteFactory: NoteFactory

    @Before
    fun init() {
        hiltRule.inject()
        mockSetup.init()
    }

    /**
     * This test runs first. Check to make sure the test data was inserted from
     * CacheTest class.
     */
    @Test
    fun a_searchNotes_confirmDbNotEmpty() = runBlocking {
        val numNotes = noteCacheDataSource.getAllNotes().size
        assertTrue { numNotes > 0 }
    }

    @Test
    fun insertNote_CBS() = runBlocking {

        val newNote = noteFactory.createNote()
        noteCacheDataSource.insertNotes(listOf(newNote))

        val cacheNotes = noteCacheDataSource.getAllNotes()
        assert(cacheNotes.contains(newNote))
    }

    @Test
    fun insertNoteList_CBS() = runBlocking {

        val noteList = noteFactory.createNotes(10)
        noteCacheDataSource.insertNotes(noteList)

        val cacheNotes = noteCacheDataSource.getAllNotes()
        assertTrue { cacheNotes.containsAll(noteList) }
    }

    @Test
    fun insert1000Notes_confirmNumNotesInDb() = runBlocking {
        val currentNumNotes = noteCacheDataSource.getAllNotes().size

        // insert 1000 notes
        val noteList = noteFactory.createNotes(1000)
        noteCacheDataSource.insertNotes(noteList)

        val numNotes = noteCacheDataSource.getAllNotes().size
        assertEquals(currentNumNotes + 1000, numNotes)
    }

    @Test
    fun insert1000Notes_searchNotesByTitle_confirm50ExpectedValues() = runBlocking {

        // insert 1000 notes
        val noteList = noteFactory.createNotes(1000)
        noteCacheDataSource.insertNotes(noteList)

        // query 50 notes by specific title
        repeat(50) {
            val randomIndex = Random.nextInt(0, noteList.size - 1)
            val result = noteCacheDataSource.searchNotes(
                query = noteList[randomIndex].title,
                page = 1,
                filterAndOrder = ORDER_BY_ASC_TITLE
            ).first()
            assertEquals(noteList[randomIndex].title, result[0].title)
        }
    }


    @Test
    fun insertNote_deleteNote_confirmDeleted() = runBlocking {
        val newNote = noteFactory.createNote()
        noteCacheDataSource.insertNotes(listOf(newNote))

        var notes = noteCacheDataSource.getAllNotes()
        assert(notes.contains(newNote))

        noteCacheDataSource.deleteNotes(listOf(newNote.id))
        notes = noteCacheDataSource.getAllNotes()
        assert(!notes.contains(newNote))
    }

    @Test
    fun deleteNoteList_confirmDeleted() = runBlocking {
        val noteList: ArrayList<Note> = ArrayList(noteCacheDataSource.getAllNotes())

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

        noteCacheDataSource.deleteNotes(notesToDelete.map { it.id })

        // confirm they were deleted
        val searchResults = noteCacheDataSource.getAllNotes()
        assertFalse { searchResults.containsAll(notesToDelete) }
    }

    @Test
    fun insertNote_updateNote_confirmUpdated() = runBlocking {
        val newNote = noteFactory.createNote()
        noteCacheDataSource.insertNotes(listOf(newNote))

        // so update timestamp will be different
        delay(1001)

        val newTitle = UUID.randomUUID().toString()
        val newBody = UUID.randomUUID().toString()
        noteCacheDataSource.updateNotes(
            listOf(
                newNote.copy(
                    title = newTitle,
                    body = newBody,
                    updatedAt = dateUtil.getCurrentTimestamp()
                )
            )
        )

        val notes = noteCacheDataSource.getAllNotes()

        var foundNote = false
        for (note in notes) {
            if (note.id == newNote.id) {
                foundNote = true
                assertEquals(newNote.id, note.id)
                assertEquals(newTitle, note.title)
                assertEquals(newBody, note.body)
                assert(newNote.updatedAt != note.updatedAt)
                assertEquals(newNote.createdAt, note.createdAt)
                break
            }
        }
        assertTrue { foundNote }
    }

    @Test
    fun searchNotes_orderByDateASC_confirmOrder() = runBlocking {
        val noteList = noteCacheDataSource.searchNotes(
            query = "",
            page = 1,
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED
        ).first()

        // check that the date gets larger (newer) as iterate down the list
        var previousNoteDate = noteList[0].createdAt
        for (index in 1 until noteList.size) {
            val currentNoteDate = noteList[index].createdAt
            assertTrue { currentNoteDate >= previousNoteDate }
            previousNoteDate = currentNoteDate
        }
    }

    @Test
    fun searchNotes_orderByDateDESC_confirmOrder() = runBlocking {
        val noteList = noteCacheDataSource.searchNotes(
            query = "",
            page = 1,
            filterAndOrder = ORDER_BY_DESC_DATE_UPDATED
        ).first()

        // check that the date gets smaller (older) as iterate down the list
        var previous = noteList[0].createdAt
        for (index in 1 until noteList.size) {
            val current = noteList[index].createdAt
            assertTrue { current <= previous }
            previous = current
        }
    }

    @Test
    fun searchNotes_orderByTitleASC_confirmOrder() = runBlocking {
        val noteList = noteCacheDataSource.searchNotes(
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
        val noteList = noteCacheDataSource.searchNotes(
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
package com.example.note.cache.database.dataSource

import com.example.note.cache.database.dao.NoteDao
import com.example.note.cache.database.mapper.NoteEntityMapper
import com.example.note.model.Note
import com.example.note.util.DateUtil
import com.example.note.util.NoteFactory
import com.example.note.util.ORDER_BY_DESC_DATE_UPDATED
import com.example.note.util.isUnitTest
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class NoteCacheDataSourceTest {

    private val noteDaoMock: NoteDao = mockk(relaxUnitFun = true)
    private val mapper = NoteEntityMapper()
    private val dateUtil = DateUtil()

    private lateinit var noteCacheDataSource: NoteCacheDataSourceImpl

    init {
        isUnitTest = true
    }

    @BeforeEach
    fun setUp() {
        noteCacheDataSource = NoteCacheDataSourceImpl(noteDaoMock, mapper)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(noteDaoMock)
    }

    @Nested
    inner class InsertNote {
        private lateinit var note: Note

        @BeforeEach
        fun setUp() {
            note = NoteFactory(dateUtil).createNote()
        }

        @Test
        fun `should insert note`() = runTest {
            noteCacheDataSource.insertNote(note)

            coVerify { noteDaoMock.insertNote(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteDaoMock.insertNote(any()) } throws Exception()

            assertThrows<Exception> { noteCacheDataSource.insertNote(note) }
        }
    }

    @Nested
    inner class InsertNotes {
        private lateinit var notes: List<Note>

        @BeforeEach
        fun setUp() {
            notes = NoteFactory(dateUtil).createNotes(5)
        }

        @Test
        fun `should insert all notes`() = runTest {
            coEvery { noteDaoMock.insertNotes(any()) } returns longArrayOf(1, 2, 3, 4, 5)

            noteCacheDataSource.insertNotes(notes)

            coVerify { noteDaoMock.insertNotes(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteDaoMock.insertNotes(any()) } throws Exception()

            assertThrows<Exception> { noteCacheDataSource.insertNotes(notes) }
        }

        @Test
        fun `when notes are empty`() = runTest {
            noteCacheDataSource.insertNotes(listOf())

            coVerify(inverse = true) { noteDaoMock.insertNotes(any()) }
        }
    }

    @Nested
    inner class UpdateNote {
        private lateinit var note: Note

        @BeforeEach
        fun setUp() {
            note = NoteFactory(dateUtil).createNote()
        }

        @Test
        fun `should update note`() = runTest {
            noteCacheDataSource.updateNote(note)

            coVerify { noteDaoMock.updateNote(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteDaoMock.updateNote(any()) } throws Exception()

            assertThrows<Exception> { noteCacheDataSource.updateNote(note) }
        }
    }

    @Nested
    inner class UpdateNotes {
        private lateinit var notes: List<Note>

        @BeforeEach
        fun setUp() {
            notes = NoteFactory(dateUtil).createNotes(5)
        }

        @Test
        fun `should update all notes`() = runTest {
            coEvery { noteDaoMock.updateNotes(any()) } returns 2

            noteCacheDataSource.updateNotes(notes)

            coVerify { noteDaoMock.updateNotes(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteDaoMock.updateNotes(any()) } throws Exception()

            assertThrows<Exception> { noteCacheDataSource.updateNotes(notes) }
        }

        @Test
        fun `when notes are empty`() = runTest {
            noteCacheDataSource.updateNotes(listOf())

            coVerify(inverse = true) { noteDaoMock.updateNotes(any()) }
        }
    }

    @Nested
    inner class GetNote {
        private lateinit var note: Note

        @BeforeEach
        fun setUp() {
            note = NoteFactory(dateUtil).createNote()
        }

        @Test
        fun `should return note`() = runTest {
            val entity = mapper.fromModel(note)
            coEvery { noteDaoMock.getNote(any()) } returns entity

            val result = noteCacheDataSource.getNote("")

            assert(result?.equals(note) ?: false)
            coVerify { noteDaoMock.getNote(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteDaoMock.getNote(any()) } throws Exception()

            assertThrows<Exception> { noteCacheDataSource.getNote("") }
        }

        @Test
        fun `when note does not exist`() = runTest {
            coEvery { noteDaoMock.getNote(any()) } returns null

            val result = noteCacheDataSource.getNote("")

            assert(result == null)
            coVerify { noteDaoMock.getNote(any()) }
        }
    }

    @Nested
    inner class GetAllNotes {
        private lateinit var notes: List<Note>

        @BeforeEach
        fun setUp() {
            notes = NoteFactory(dateUtil).createNotes(5)
        }

        @Test
        fun `should return all notes`() = runTest {
            val entities = notes.map { mapper.fromModel(it) }
            coEvery { noteDaoMock.getAllNotes() } returns entities

            val result = noteCacheDataSource.getAllNotes()

            assert(result == notes)
            coVerify { noteDaoMock.getAllNotes() }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteDaoMock.getAllNotes() } throws Exception()

            assertThrows<Exception> { noteCacheDataSource.getAllNotes() }
        }
    }

    @Nested
    inner class DeleteNote {

        @Test
        fun `should delete note`() = runTest {
            noteCacheDataSource.deleteNote("")

            coVerify { noteDaoMock.deleteNote(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteDaoMock.deleteNote(any()) } throws Exception()

            assertThrows<Exception> { noteCacheDataSource.deleteNote("") }
        }
    }

    @Nested
    inner class DeleteNotes {
        @Test
        fun `should delete all notes`() = runTest {
            coEvery { noteDaoMock.deleteNotes(any()) } returns 2

            noteCacheDataSource.deleteNotes(listOf("1", "2"))

            coVerify { noteDaoMock.deleteNotes(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteDaoMock.deleteNotes(any()) } throws Exception()

            assertThrows<Exception> { noteCacheDataSource.deleteNotes(listOf("1", "2")) }
        }

        @Test
        fun `when notes are empty`() = runTest {
            noteCacheDataSource.deleteNotes(listOf())

            coVerify(inverse = true) { noteDaoMock.deleteNotes(any()) }
        }
    }

    @Nested
    inner class SearchNotes {

        @Test
        fun `should return notes`() = runTest {
            val notes = NoteFactory(dateUtil).createNotes(5)
            val entity = notes.map { mapper.fromModel(it) }
            coEvery {
                noteDaoMock.searchNotesOrderByDateDESC(any(), any(), any())
            } returns flow { emit(entity) }

            val result = noteCacheDataSource
                .searchNotes("", ORDER_BY_DESC_DATE_UPDATED, 1)
                .first()

            assert(result == notes)
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery {
                noteDaoMock.searchNotesOrderByDateDESC(any(), any(), any())
            } throws Exception()

            assertThrows<Exception> {
                noteCacheDataSource.searchNotes("", ORDER_BY_DESC_DATE_UPDATED, 1)
            }
        }
    }
}
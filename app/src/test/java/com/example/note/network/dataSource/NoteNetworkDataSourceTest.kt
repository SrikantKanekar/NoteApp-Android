package com.example.note.network.dataSource

import com.example.note.model.Note
import com.example.note.network.api.NoteApi
import com.example.note.network.mapper.NoteDtoMapper
import com.example.note.util.DateUtil
import com.example.note.util.NoteFactory
import com.example.note.util.isUnitTest
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class NoteNetworkDataSourceTest {

    private val noteApiMock: NoteApi = mockk()
    private val mapper = NoteDtoMapper()
    private val dateUtil = DateUtil()

    private lateinit var noteNetworkDataSource: NoteNetworkDataSourceImpl

    init {
        isUnitTest = true
    }

    @BeforeEach
    fun setUp() {
        noteNetworkDataSource = NoteNetworkDataSourceImpl(noteApiMock, mapper)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(noteApiMock)
    }

    @Nested
    inner class InsertOrUpdateNotes {
        private lateinit var notes: List<Note>

        @BeforeEach
        fun setUp() {
            notes = NoteFactory(dateUtil).createNotes(5)
        }

        @Test
        fun `should insert or update all notes`() = runTest {
            coEvery { noteApiMock.insertOrUpdateNotes(any()) } returns Unit

            noteNetworkDataSource.insertOrUpdateNotes(notes)

            coVerify { noteApiMock.insertOrUpdateNotes(any()) }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { noteApiMock.insertOrUpdateNotes(any()) } throws Exception()

            assertThrows<Exception> { noteNetworkDataSource.insertOrUpdateNotes(notes) }
        }

        @Test
        fun `when notes are empty`() = runTest {
            noteNetworkDataSource.insertOrUpdateNotes(listOf())

            coVerify(inverse = true) { noteApiMock.insertOrUpdateNotes(any()) }
        }
    }

    @Nested
    inner class DeleteNotes {
        private lateinit var noteIds: List<String>

        @BeforeEach
        fun setUp() {
            val notes = NoteFactory(dateUtil).createNotes(5)
            noteIds = notes.map { it.id }
        }

        @Test
        fun `should delete notes`() = runTest {
            coEvery { noteApiMock.deleteNotes(any()) } returns Unit

            noteNetworkDataSource.deleteNotes(noteIds)

            coVerify { noteApiMock.deleteNotes(any()) }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { noteApiMock.deleteNotes(any()) } throws Exception()

            assertThrows<Exception> { noteNetworkDataSource.deleteNotes(noteIds) }
        }

        @Test
        fun `when notes are empty`() = runTest {
            noteNetworkDataSource.deleteNotes(listOf())

            coVerify(inverse = true) { noteApiMock.deleteNotes(any()) }
        }
    }

    @Nested
    inner class GetNote {
        private val noteId = "NOTE_ID"
        private lateinit var note: Note

        @BeforeEach
        fun setUp() {
            note = NoteFactory(dateUtil).createNote(id = noteId)
        }

        @Test
        fun `should return note`() = runTest {
            val noteDto = mapper.fromModel(note)
            coEvery { noteApiMock.getNote(noteId) } returns noteDto

            val result = noteNetworkDataSource.getNote(noteId)

            assert(result == note)
            coVerify { noteApiMock.getNote(noteId) }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { noteApiMock.getNote(noteId) } throws Exception()

            assertThrows<Exception> { noteNetworkDataSource.getNote(noteId) }
        }

        @Test
        fun `when note does not exist`() = runTest {
            coEvery { noteApiMock.getNote(noteId) } returns null

            val result = noteNetworkDataSource.getNote(noteId)

            assert(result == null)
            coVerify { noteApiMock.getNote(noteId) }
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
            val noteDtoList = notes.map { mapper.fromModel(it) }
            coEvery { noteApiMock.getAllNotes() } returns noteDtoList

            val result = noteNetworkDataSource.getAllNotes()

            assert(result == notes)
            coVerify { noteApiMock.getAllNotes() }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { noteApiMock.getAllNotes() } throws Exception()

            assertThrows<Exception> { noteNetworkDataSource.getAllNotes() }
        }
    }
}
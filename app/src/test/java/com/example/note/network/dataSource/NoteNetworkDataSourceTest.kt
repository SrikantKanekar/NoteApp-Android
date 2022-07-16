package com.example.note.network.dataSource

import com.example.note.model.Note
import com.example.note.network.api.NoteApi
import com.example.note.network.mapper.NoteDtoMapper
import com.example.note.network.response.SimpleResponse
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

    val successResponse = SimpleResponse(true, "success")

    @AfterEach
    fun tearDown() {
        clearMocks(noteApiMock)
    }

    @Nested
    inner class InsertOrUpdateNote {
        private lateinit var note: Note

        @BeforeEach
        fun setUp() {
            note = NoteFactory(dateUtil).createSingleNote()
        }

        @Test
        fun `should insert or update note`() = runTest {
            coEvery { noteApiMock.insertOrUpdateNote(any()) } returns successResponse

            noteNetworkDataSource.insertOrUpdateNote(note)

            coVerify { noteApiMock.insertOrUpdateNote(any()) }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { noteApiMock.insertOrUpdateNote(any()) } throws Exception()

            assertThrows<Exception> { noteNetworkDataSource.insertOrUpdateNote(note) }
        }
    }

    @Nested
    inner class InsertOrUpdateNotes {
        private lateinit var notes: List<Note>

        @BeforeEach
        fun setUp() {
            notes = NoteFactory(dateUtil).createNoteList(5)
        }

        @Test
        fun `should insert or update all notes`() = runTest {
            coEvery { noteApiMock.insertOrUpdateNotes(any()) } returns successResponse

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
    inner class GetNote {
        private lateinit var note: Note

        @BeforeEach
        fun setUp() {
            note = NoteFactory(dateUtil).createSingleNote()
        }

        @Test
        fun `should return note`() = runTest {
            val noteDto = mapper.fromModel(note)
            coEvery { noteApiMock.getNote(any()) } returns noteDto

            val result = noteNetworkDataSource.getNote("")

            assert(result?.equals(note) ?: false)
            coVerify { noteApiMock.getNote(any()) }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { noteApiMock.getNote(any()) } throws Exception()

            assertThrows<Exception> { noteNetworkDataSource.getNote("") }
        }

        @Test
        fun `when note does not exist`() = runTest {
            coEvery { noteApiMock.getNote(any()) } returns null

            val result = noteNetworkDataSource.getNote("")

            assert(result == null)
            coVerify { noteApiMock.getNote(any()) }
        }
    }

    @Nested
    inner class GetAllNotes {
        private lateinit var notes: List<Note>

        @BeforeEach
        fun setUp() {
            notes = NoteFactory(dateUtil).createNoteList(5)
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

    @Nested
    inner class DeleteNote {

        @Test
        fun `should delete note`() = runTest {
            coEvery { noteApiMock.deleteNote(any()) } returns successResponse

            noteNetworkDataSource.deleteNote("")

            coVerify { noteApiMock.deleteNote(any()) }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { noteApiMock.deleteNote(any()) } throws Exception()

            assertThrows<Exception> { noteNetworkDataSource.deleteNote("") }
        }
    }
}
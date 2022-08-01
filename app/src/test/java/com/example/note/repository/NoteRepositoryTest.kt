package com.example.note.repository

import com.example.note.cache.database.dataSource.NoteCacheDataSource
import com.example.note.model.Note
import com.example.note.network.dataSource.NoteNetworkDataSource
import com.example.note.util.DateUtil
import com.example.note.util.NoteFactory
import com.example.note.util.isUnitTest
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class NoteRepositoryTest {

    private val noteCacheMock: NoteCacheDataSource = mockk(relaxUnitFun = true)
    private val noteNetworkMock: NoteNetworkDataSource = mockk(relaxUnitFun = true)
    private val dateUtil = DateUtil()

    private lateinit var repository: NoteRepositoryImpl

    init {
        isUnitTest = true
    }

    @BeforeEach
    fun setUp() {
        repository = NoteRepositoryImpl(noteCacheMock, noteNetworkMock, dateUtil)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(noteCacheMock, noteNetworkMock)
    }

    @Nested
    inner class InsertNote {
        @Test
        fun `should insert note`() = runTest {
            repository.insertNote(mockk())

            coVerifySequence {
                noteCacheMock.insertNote(any())
                noteNetworkMock.insertOrUpdateNote(any())
            }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteCacheMock.insertNote(any()) } throws Exception()

            assertThrows<Exception> { repository.insertNote(mockk()) }

            coVerify { noteCacheMock.insertNote(any()) }
            coVerify(inverse = true) { noteNetworkMock.insertOrUpdateNote(any()) }
        }

        @Test
        fun `when Api call fails`() = runTest {
            coEvery { noteNetworkMock.insertOrUpdateNote(any()) } throws Exception()

            assertThrows<Exception> { repository.insertNote(mockk()) }

            coVerifySequence {
                noteCacheMock.insertNote(any())
                noteNetworkMock.insertOrUpdateNote(any())
            }
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
            repository.updateNote(note)

            coVerifyOrder {
                noteCacheMock.updateNote(any())
                noteNetworkMock.insertOrUpdateNote(any())
            }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteCacheMock.updateNote(any()) } throws Exception()

            assertThrows<Exception> { repository.updateNote(note) }

            coVerify { noteCacheMock.updateNote(any()) }
            coVerify(inverse = true) { noteNetworkMock.insertOrUpdateNote(any()) }
        }

        @Test
        fun `when Api call fails`() = runTest {
            coEvery { noteNetworkMock.insertOrUpdateNote(any()) } throws Exception()

            assertThrows<Exception> { repository.updateNote(note) }

            coVerifyOrder {
                noteCacheMock.updateNote(any())
                noteNetworkMock.insertOrUpdateNote(any())
            }
        }
    }

    @Nested
    inner class GetNote {

        @Test
        fun `should get note`() = runTest {
            val note = NoteFactory(dateUtil).createNote()
            coEvery { noteCacheMock.getNote(any()) } returns note

            val result = repository.getNote("")

            assert(result == note)
            coVerify { noteCacheMock.getNote(any()) }
        }

        @Test
        fun `when note doesn't exist`() = runTest {
            coEvery { noteCacheMock.getNote(any()) } returns null

            assertThrows<Exception> { repository.getNote("") }

            coVerify { noteCacheMock.getNote(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteCacheMock.getNote(any()) } throws Exception()

            assertThrows<Exception> { repository.getNote("") }

            coVerify { noteCacheMock.getNote(any()) }
        }
    }

    @Nested
    inner class SearchNotes {

        @Test
        fun `should return flow of notes`() = runTest {
            val notes = NoteFactory(dateUtil).createNotes(5)
            val flow = flow { emit(notes) }
            every { noteCacheMock.searchNotes(any(), any(), any()) } returns flow

            val result = repository.searchNotes("").first()

            assert(result == notes)
            verify { noteCacheMock.searchNotes(any(), any(), any()) }
        }

        @Test
        fun `when flow throws exception`() = runTest {
            every { noteCacheMock.searchNotes(any(), any(), any()) } throws Exception()

            assertThrows<Exception> { repository.searchNotes("") }

            verify { noteCacheMock.searchNotes(any(), any(), any()) }
        }
    }

    @Nested
    inner class SyncNotes {
        private lateinit var note: Note
        private val noteFactory = NoteFactory(dateUtil)

        @BeforeEach
        fun setUp() {
            note = noteFactory.createNote()
        }

        @Test
        fun `should insert note into cache if cache is empty`() = runTest {
            val networkNotes = noteFactory.createNote()
            coEvery { noteNetworkMock.getAllNotes() } returns listOf(networkNotes)
            coEvery { noteCacheMock.getAllNotes() } returns listOf()
            coEvery { noteCacheMock.getNote(any()) } returns null

            repository.syncNotes()

            coVerify { noteCacheMock.insertNotes(listOf(networkNotes)) }
        }

        @Test
        fun `should insert note into network if network is empty`() = runTest {
            val cacheNotes = noteFactory.createNote()
            coEvery { noteCacheMock.getAllNotes() } returns listOf(cacheNotes)
            coEvery { noteNetworkMock.getAllNotes() } returns listOf()

            repository.syncNotes()

            coVerify { noteNetworkMock.insertOrUpdateNotes(listOf(cacheNotes)) }
        }

        @Test
        fun `should insert note into cache if network has latest`() = runTest {
            val oldNote = noteFactory.createYesterdayNote(id = "id")
            val newNote = noteFactory.createNote(id = "id")
            coEvery { noteNetworkMock.getAllNotes() } returns listOf(newNote)
            coEvery { noteCacheMock.getAllNotes() } returns listOf(oldNote)
            coEvery { noteCacheMock.getNote(any()) } returns oldNote

            repository.syncNotes()

            coVerify { noteCacheMock.updateNotes(listOf(newNote)) }
        }

        @Test
        fun `should insert note into network if cache has latest`() = runTest {
            val oldNote = noteFactory.createYesterdayNote(id = "id")
            val newNote = noteFactory.createNote(id = "id")
            coEvery { noteNetworkMock.getAllNotes() } returns listOf(oldNote)
            coEvery { noteCacheMock.getAllNotes() } returns listOf(newNote)
            coEvery { noteCacheMock.getNote(any()) } returns newNote

            repository.syncNotes()

            coVerify { noteNetworkMock.insertOrUpdateNotes(listOf(newNote)) }
        }

        @Nested
        @DisplayName("when exception is thrown")
        inner class Exceptions {
            @Test
            fun `for network get all notes call`() = runTest {
                coEvery { noteNetworkMock.getAllNotes() } throws Exception()

                assertThrows<Exception> { repository.syncNotes() }

                coVerify(inverse = true) { noteCacheMock.getAllNotes() }
            }

            @Test
            fun `for cache get all notes call`() = runTest {
                coEvery { noteNetworkMock.getAllNotes() } returns listOf()
                coEvery { noteCacheMock.getAllNotes() } throws Exception()

                assertThrows<Exception> { repository.syncNotes() }

                coVerify(inverse = true) { noteCacheMock.insertNotes(any()) }
            }

            /**
             * assume two new notes in network
             */
            @Test
            fun `for cache get note call`() = runTest {
                val note1 = noteFactory.createNote(id = "1")
                val note2 = noteFactory.createNote(id = "2")

                coEvery { noteNetworkMock.getAllNotes() } returns listOf(note1, note2)
                coEvery { noteCacheMock.getAllNotes() } returns listOf()
                coEvery { noteCacheMock.getNote("1") } returns null // not present in db
                coEvery { noteCacheMock.getNote("2") } throws Exception()

                repository.syncNotes()

                coVerify { noteCacheMock.insertNotes(listOf(note1)) }
            }

            @Test
            fun `for cache insert notes call`() = runTest {
                coEvery { noteNetworkMock.getAllNotes() } returns listOf()
                coEvery { noteCacheMock.getAllNotes() } returns listOf()
                coEvery { noteCacheMock.insertNotes(any()) } throws Exception()

                repository.syncNotes()

                coVerify { noteCacheMock.updateNotes(any()) }
            }

            @Test
            fun `for cache update notes call`() = runTest {
                coEvery { noteNetworkMock.getAllNotes() } returns listOf()
                coEvery { noteCacheMock.getAllNotes() } returns listOf()
                coEvery { noteCacheMock.updateNotes(any()) } throws Exception()

                repository.syncNotes()

                coVerify { noteNetworkMock.insertOrUpdateNotes(any()) }
            }

            @Test
            fun `for network insert or update notes call`() = runTest {
                coEvery { noteNetworkMock.getAllNotes() } returns listOf()
                coEvery { noteCacheMock.getAllNotes() } returns listOf()
                coEvery { noteNetworkMock.insertOrUpdateNotes(any()) } throws Exception()

                repository.syncNotes()
            }
        }
    }

    @Nested
    inner class DeleteNote {
        private lateinit var note: Note

        @BeforeEach
        fun setUp() {
            note = NoteFactory(dateUtil).createNote()
        }

        @Test
        fun `should delete note`() = runTest {
            repository.updateNoteState(note)

            coVerifyOrder {
                noteCacheMock.updateNote(any())
                noteNetworkMock.insertOrUpdateNote(any())
            }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteCacheMock.updateNote(any()) } throws Exception()

            assertThrows<Exception> { repository.updateNoteState(note) }

            coVerify { noteCacheMock.updateNote(any()) }
            coVerify(inverse = true) { noteNetworkMock.insertOrUpdateNote(any()) }
        }

        @Test
        fun `when Api call fails`() = runTest {
            coEvery { noteNetworkMock.insertOrUpdateNote(any()) } throws Exception()

            assertThrows<Exception> { repository.updateNoteState(note) }

            coVerifyOrder {
                noteCacheMock.updateNote(any())
                noteNetworkMock.insertOrUpdateNote(any())
            }
        }
    }

    @Nested
    inner class DeleteNotes {
        private lateinit var notes: List<Note>

        @BeforeEach
        fun setUp() {
            notes = NoteFactory(dateUtil).createNotes(5)
        }

        @Test
        fun `should delete notes`() = runTest {
            repository.deleteNotes(notes)

            coVerifyOrder {
                noteCacheMock.updateNotes(any())
                noteNetworkMock.insertOrUpdateNotes(any())
            }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteCacheMock.updateNotes(any()) } throws Exception()

            assertThrows<Exception> { repository.deleteNotes(notes) }

            coVerify { noteCacheMock.updateNotes(any()) }
            coVerify(inverse = true) { noteNetworkMock.insertOrUpdateNotes(any()) }
        }

        @Test
        fun `when Api call fails`() = runTest {
            coEvery { noteNetworkMock.insertOrUpdateNotes(any()) } throws Exception()

            assertThrows<Exception> { repository.deleteNotes(notes) }

            coVerifyOrder {
                noteCacheMock.updateNotes(any())
                noteNetworkMock.insertOrUpdateNotes(any())
            }
        }
    }

    @Nested
    inner class RestoreDeletedNote {
        private lateinit var note: Note

        @BeforeEach
        fun setUp() {
            note = NoteFactory(dateUtil).createNote()
        }

        @Test
        fun `should restore deleted note`() = runTest {
            repository.restoreDeletedNote(note)

            coVerifyOrder {
                noteCacheMock.updateNote(any())
                noteNetworkMock.insertOrUpdateNote(any())
            }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { noteCacheMock.updateNote(any()) } throws Exception()

            assertThrows<Exception> { repository.restoreDeletedNote(note) }

            coVerify { noteCacheMock.updateNote(any()) }
            coVerify(inverse = true) { noteNetworkMock.insertOrUpdateNote(any()) }
        }

        @Test
        fun `when Api call fails`() = runTest {
            coEvery { noteNetworkMock.insertOrUpdateNote(any()) } throws Exception()

            assertThrows<Exception> { repository.restoreDeletedNote(note) }

            coVerifyOrder {
                noteCacheMock.updateNote(any())
                noteNetworkMock.insertOrUpdateNote(any())
            }
        }
    }
}
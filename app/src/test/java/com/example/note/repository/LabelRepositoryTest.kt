package com.example.note.repository

import com.example.note.cache.database.dataSource.LabelCacheDataSource
import com.example.note.model.Label
import com.example.note.network.dataSource.LabelNetworkDataSource
import com.example.note.util.DateUtil
import com.example.note.util.LabelFactory
import com.example.note.util.isUnitTest
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class LabelRepositoryTest {

    private val labelCacheMock: LabelCacheDataSource = mockk(relaxUnitFun = true)
    private val labelNetworkMock: LabelNetworkDataSource = mockk(relaxUnitFun = true)
    private val dateUtil = DateUtil()

    private lateinit var repository: LabelRepositoryImpl

    init {
        isUnitTest = true
    }

    @BeforeEach
    fun setUp() {
        repository = LabelRepositoryImpl(labelCacheMock, labelNetworkMock, dateUtil)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(labelCacheMock, labelNetworkMock)
    }

    @Nested
    inner class InsertLabels {
        @Test
        fun `should insert labels`() = runTest {
            repository.insertLabels(mockk())

            coVerifySequence {
                labelCacheMock.insertLabels(any())
                labelNetworkMock.insertOrUpdateLabels(any())
            }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelCacheMock.insertLabels(any()) } throws Exception()

            assertThrows<Exception> { repository.insertLabels(mockk()) }

            coVerify { labelCacheMock.insertLabels(any()) }
            coVerify(inverse = true) { labelNetworkMock.insertOrUpdateLabels(any()) }
        }

        @Test
        fun `when Api call fails`() = runTest {
            coEvery { labelNetworkMock.insertOrUpdateLabels(any()) } throws Exception()

            assertThrows<Exception> { repository.insertLabels(mockk()) }

            coVerifySequence {
                labelCacheMock.insertLabels(any())
                labelNetworkMock.insertOrUpdateLabels(any())
            }
        }
    }

    @Nested
    inner class UpdateLabels {
        private lateinit var labels: List<Label>

        @BeforeEach
        fun setUp() {
            labels = LabelFactory(dateUtil).createLabels(5)
        }

        @Test
        fun `should update labels`() = runTest {
            repository.updateLabels(labels)

            coVerifyOrder {
                labelCacheMock.updateLabels(any())
                labelNetworkMock.insertOrUpdateLabels(any())
            }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelCacheMock.updateLabels(any()) } throws Exception()

            assertThrows<Exception> { repository.updateLabels(labels) }

            coVerify { labelCacheMock.updateLabels(any()) }
            coVerify(inverse = true) { labelNetworkMock.insertOrUpdateLabels(any()) }
        }

        @Test
        fun `when Api call fails`() = runTest {
            coEvery { labelNetworkMock.insertOrUpdateLabels(any()) } throws Exception()

            assertThrows<Exception> { repository.updateLabels(labels) }

            coVerifyOrder {
                labelCacheMock.updateLabels(any())
                labelNetworkMock.insertOrUpdateLabels(any())
            }
        }
    }

    @Nested
    inner class DeleteLabels {
        private lateinit var labels: List<Label>

        @BeforeEach
        fun setUp() {
            labels = LabelFactory(dateUtil).createLabels(5)
        }

        @Test
        fun `should delete labels`() = runTest {
            repository.deleteLabels(labels)

            coVerifyOrder {
                labelCacheMock.deleteLabels(any())
                labelNetworkMock.deleteLabels(any())
            }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelCacheMock.deleteLabels(any()) } throws Exception()

            assertThrows<Exception> { repository.deleteLabels(labels) }

            coVerify { labelCacheMock.deleteLabels(any()) }
            coVerify(inverse = true) { labelNetworkMock.deleteLabels(any()) }
        }

        @Test
        fun `when Api call fails`() = runTest {
            coEvery { labelNetworkMock.deleteLabels(any()) } throws Exception()

            assertThrows<Exception> { repository.deleteLabels(labels) }

            coVerifyOrder {
                labelCacheMock.deleteLabels(any())
                labelNetworkMock.deleteLabels(any())
            }
        }
    }

    @Nested
    inner class GetLabel {
        private val labelId = "LABEL_ID"

        @Test
        fun `should get label`() = runTest {
            val label = LabelFactory(dateUtil).createLabel(labelId, "labelName")
            coEvery { labelCacheMock.getLabel(labelId) } returns label

            val result = repository.getLabel(labelId)

            assert(result == label)
            coVerify { labelCacheMock.getLabel(labelId) }
        }

        @Test
        fun `when label doesn't exist`() = runTest {
            coEvery { labelCacheMock.getLabel(labelId) } returns null

            assertThrows<Exception> { repository.getLabel(labelId) }

            coVerify { labelCacheMock.getLabel(labelId) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelCacheMock.getLabel(labelId) } throws Exception()

            assertThrows<Exception> { repository.getLabel(labelId) }

            coVerify { labelCacheMock.getLabel(labelId) }
        }
    }

    @Nested
    inner class GetAllLabels {

        @Test
        fun `should get all label`() = runTest {
            val labels = LabelFactory(dateUtil).createLabels(5)
            coEvery { labelCacheMock.getAllLabels() } returns labels

            val result = repository.getAllLabels()

            assert(result == labels)
            coVerify { labelCacheMock.getAllLabels() }
        }

        @Test
        fun `when labels are empty`() = runTest {
            coEvery { labelCacheMock.getAllLabels() } returns emptyList()

            val result = repository.getAllLabels()

            assert(result.isEmpty())
            coVerify { labelCacheMock.getAllLabels() }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelCacheMock.getAllLabels() } throws Exception()

            assertThrows<Exception> { repository.getAllLabels() }

            coVerify { labelCacheMock.getAllLabels() }
        }
    }

    @Nested
    inner class SearchLabels {

        @Test
        fun `should return flow of labels`() = runTest {
            val labels = LabelFactory(dateUtil).createLabels(5)
            val flow = flow { emit(labels) }
            every { labelCacheMock.searchLabels(any()) } returns flow

            val result = repository.searchLabels("").first()

            assert(result == labels)
            verify { labelCacheMock.searchLabels(any()) }
        }

        @Test
        fun `when flow throws exception`() = runTest {
            every { labelCacheMock.searchLabels(any()) } throws Exception()

            assertThrows<Exception> { repository.searchLabels("") }

            verify { labelCacheMock.searchLabels(any()) }
        }
    }

    @Nested
    inner class SyncLabels {
        private lateinit var label: Label
        private val labelFactory = LabelFactory(dateUtil)

        @BeforeEach
        fun setUp() {
            label = labelFactory.createLabel(name = "labelName")
        }

        @Test
        fun `should insert label into cache if cache is empty`() = runTest {
            val networkLabels = labelFactory.createLabels(10)
            coEvery { labelNetworkMock.getAllLabels() } returns networkLabels
            coEvery { labelCacheMock.getAllLabels() } returns listOf()
            coEvery { labelCacheMock.getLabel(any()) } returns null

            repository.syncLabels()

            coVerify { labelCacheMock.insertLabels(networkLabels) }
        }

        @Test
        fun `should insert label into network if network is empty`() = runTest {
            val cacheLabels = labelFactory.createLabels(10)
            coEvery { labelCacheMock.getAllLabels() } returns cacheLabels
            coEvery { labelNetworkMock.getAllLabels() } returns listOf()

            repository.syncLabels()

            coVerify { labelNetworkMock.insertOrUpdateLabels(cacheLabels) }
        }

        @Test
        fun `should insert label into cache if network has latest`() = runTest {
            val oldLabel = labelFactory.createYesterdayLabel(id = "id")
            val newLabel = labelFactory.createLabel(id = "id", name = "labelName")
            coEvery { labelCacheMock.getAllLabels() } returns listOf(oldLabel)
            coEvery { labelNetworkMock.getAllLabels() } returns listOf(newLabel)
            coEvery { labelCacheMock.getLabel(any()) } returns oldLabel

            repository.syncLabels()

            coVerify { labelCacheMock.updateLabels(listOf(newLabel)) }
        }

        @Test
        fun `should insert label into network if cache has latest`() = runTest {
            val oldLabel = labelFactory.createYesterdayLabel(id = "id")
            val newLabel = labelFactory.createLabel(id = "id", name = "labelName")
            coEvery { labelNetworkMock.getAllLabels() } returns listOf(oldLabel)
            coEvery { labelCacheMock.getAllLabels() } returns listOf(newLabel)
            coEvery { labelCacheMock.getLabel(any()) } returns newLabel

            repository.syncLabels()

            coVerify { labelNetworkMock.insertOrUpdateLabels(listOf(newLabel)) }
        }

        @Nested
        @DisplayName("when exception is thrown")
        inner class Exceptions {
            @Test
            fun `for network get all labels call`() = runTest {
                coEvery { labelNetworkMock.getAllLabels() } throws Exception()

                assertThrows<Exception> { repository.syncLabels() }

                coVerify(inverse = true) { labelCacheMock.getAllLabels() }
            }

            @Test
            fun `for cache get all labels call`() = runTest {
                coEvery { labelNetworkMock.getAllLabels() } returns listOf()
                coEvery { labelCacheMock.getAllLabels() } throws Exception()

                assertThrows<Exception> { repository.syncLabels() }

                coVerify(inverse = true) { labelCacheMock.insertLabels(any()) }
            }

            /**
             * assume two new labels in network
             */
            @Test
            fun `for cache get label call`() = runTest {
                val label1 = labelFactory.createLabel(id = "1", name = "labelName1")
                val label2 = labelFactory.createLabel(id = "2", name = "labelName2")

                coEvery { labelNetworkMock.getAllLabels() } returns listOf(label1, label2)
                coEvery { labelCacheMock.getAllLabels() } returns listOf()
                coEvery { labelCacheMock.getLabel("1") } returns null // not present in db
                coEvery { labelCacheMock.getLabel("2") } throws Exception()

                repository.syncLabels()

                coVerify { labelCacheMock.insertLabels(listOf(label1)) }
            }

            @Test
            fun `for cache insert labels call`() = runTest {
                coEvery { labelNetworkMock.getAllLabels() } returns listOf()
                coEvery { labelCacheMock.getAllLabels() } returns listOf()
                coEvery { labelCacheMock.insertLabels(any()) } throws Exception()

                repository.syncLabels()

                coVerify { labelCacheMock.updateLabels(any()) }
            }

            @Test
            fun `for cache update labels call`() = runTest {
                coEvery { labelNetworkMock.getAllLabels() } returns listOf()
                coEvery { labelCacheMock.getAllLabels() } returns listOf()
                coEvery { labelCacheMock.updateLabels(any()) } throws Exception()

                repository.syncLabels()

                coVerify { labelNetworkMock.insertOrUpdateLabels(any()) }
            }

            @Test
            fun `for network insert or update labels call`() = runTest {
                coEvery { labelNetworkMock.getAllLabels() } returns listOf()
                coEvery { labelCacheMock.getAllLabels() } returns listOf()
                coEvery { labelNetworkMock.insertOrUpdateLabels(any()) } throws Exception()

                repository.syncLabels()
            }
        }
    }
}
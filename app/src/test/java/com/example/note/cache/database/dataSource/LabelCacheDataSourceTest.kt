package com.example.note.cache.database.dataSource

import com.example.note.cache.database.dao.LabelDao
import com.example.note.cache.database.mapper.LabelEntityMapper
import com.example.note.model.Label
import com.example.note.util.DateUtil
import com.example.note.util.LabelFactory
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
internal class LabelCacheDataSourceTest {

    private val labelDaoMock: LabelDao = mockk(relaxUnitFun = true)
    private val mapper = LabelEntityMapper()
    private val dateUtil = DateUtil()

    private lateinit var labelCacheDataSource: LabelCacheDataSourceImpl

    init {
        isUnitTest = true
    }

    @BeforeEach
    fun setUp() {
        labelCacheDataSource = LabelCacheDataSourceImpl(labelDaoMock, mapper)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(labelDaoMock)
    }

    @Nested
    inner class InsertLabels {
        private lateinit var labels: List<Label>

        @BeforeEach
        fun setUp() {
            labels = LabelFactory(dateUtil).createLabels(5)
        }

        @Test
        fun `should insert all labels`() = runTest {
            coEvery { labelDaoMock.insertLabels(any()) } returns longArrayOf(1, 2, 3, 4, 5)

            labelCacheDataSource.insertLabels(labels)

            coVerify { labelDaoMock.insertLabels(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelDaoMock.insertLabels(any()) } throws Exception()

            assertThrows<Exception> { labelCacheDataSource.insertLabels(labels) }
        }

        @Test
        fun `when labels are empty`() = runTest {
            labelCacheDataSource.insertLabels(listOf())

            coVerify(inverse = true) { labelDaoMock.insertLabels(any()) }
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
        fun `should update all labels`() = runTest {
            coEvery { labelDaoMock.updateLabels(any()) } returns 2

            labelCacheDataSource.updateLabels(labels)

            coVerify { labelDaoMock.updateLabels(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelDaoMock.updateLabels(any()) } throws Exception()

            assertThrows<Exception> { labelCacheDataSource.updateLabels(labels) }
        }

        @Test
        fun `when labels are empty`() = runTest {
            labelCacheDataSource.updateLabels(listOf())

            coVerify(inverse = true) { labelDaoMock.updateLabels(any()) }
        }
    }

    @Nested
    inner class DeleteLabels {
        @Test
        fun `should delete all labels`() = runTest {
            coEvery { labelDaoMock.deleteLabels(any()) } returns 2

            labelCacheDataSource.deleteLabels(listOf("1", "2"))

            coVerify { labelDaoMock.deleteLabels(any()) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelDaoMock.deleteLabels(any()) } throws Exception()

            assertThrows<Exception> { labelCacheDataSource.deleteLabels(listOf("1", "2")) }
        }

        @Test
        fun `when labels are empty`() = runTest {
            labelCacheDataSource.deleteLabels(listOf())

            coVerify(inverse = true) { labelDaoMock.deleteLabels(any()) }
        }
    }

    @Nested
    inner class GetLabel {
        private val labelId = "NOTE_ID"
        private lateinit var label: Label

        @BeforeEach
        fun setUp() {
            label = LabelFactory(dateUtil).createLabel(id = labelId, name = "labelName")
        }

        @Test
        fun `should return label`() = runTest {
            val entity = mapper.fromModel(label)
            coEvery { labelDaoMock.getLabel(labelId) } returns entity

            val result = labelCacheDataSource.getLabel(labelId)

            assert(result == label)
            coVerify { labelDaoMock.getLabel(labelId) }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelDaoMock.getLabel(labelId) } throws Exception()

            assertThrows<Exception> { labelCacheDataSource.getLabel(labelId) }
        }

        @Test
        fun `when label does not exist`() = runTest {
            coEvery { labelDaoMock.getLabel(labelId) } returns null

            val result = labelCacheDataSource.getLabel(labelId)

            assert(result == null)
            coVerify { labelDaoMock.getLabel(labelId) }
        }
    }

    @Nested
    inner class GetAllLabels {
        private lateinit var labels: List<Label>

        @BeforeEach
        fun setUp() {
            labels = LabelFactory(dateUtil).createLabels(5)
        }

        @Test
        fun `should return all labels`() = runTest {
            val entities = labels.map { mapper.fromModel(it) }
            coEvery { labelDaoMock.getAllLabels() } returns entities

            val result = labelCacheDataSource.getAllLabels()

            assert(result == labels)
            coVerify { labelDaoMock.getAllLabels() }
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery { labelDaoMock.getAllLabels() } throws Exception()

            assertThrows<Exception> { labelCacheDataSource.getAllLabels() }
        }
    }

    @Nested
    inner class SearchLabels {

        @Test
        fun `should return labels`() = runTest {
            val labels = LabelFactory(dateUtil).createLabels(5)
            val entity = labels.map { mapper.fromModel(it) }
            coEvery {
                labelDaoMock.searchLabels(any())
            } returns flow { emit(entity) }

            val result = labelCacheDataSource
                .searchLabels("")
                .first()

            assert(result == labels)
        }

        @Test
        fun `when database call fails`() = runTest {
            coEvery {
                labelDaoMock.searchLabels(any())
            } throws Exception()

            assertThrows<Exception> {
                labelCacheDataSource.searchLabels("")
            }
        }
    }
}
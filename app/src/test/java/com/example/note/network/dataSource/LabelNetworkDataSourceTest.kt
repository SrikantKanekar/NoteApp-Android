package com.example.note.network.dataSource

import com.example.note.model.Label
import com.example.note.network.api.LabelApi
import com.example.note.network.mapper.LabelDtoMapper
import com.example.note.util.DateUtil
import com.example.note.util.LabelFactory
import com.example.note.util.isUnitTest
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class LabelNetworkDataSourceTest {

    private val labelApiMock: LabelApi = mockk()
    private val mapper = LabelDtoMapper()
    private val dateUtil = DateUtil()

    private lateinit var labelNetworkDataSource: LabelNetworkDataSourceImpl

    init {
        isUnitTest = true
    }

    @BeforeEach
    fun setUp() {
        labelNetworkDataSource = LabelNetworkDataSourceImpl(labelApiMock, mapper)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(labelApiMock)
    }

    @Nested
    inner class InsertOrUpdateLabels {
        private lateinit var labels: List<Label>

        @BeforeEach
        fun setUp() {
            labels = LabelFactory(dateUtil).createLabels(5)
        }

        @Test
        fun `should insert or update all labels`() = runTest {
            coEvery { labelApiMock.insertOrUpdateLabels(any()) } returns Unit

            labelNetworkDataSource.insertOrUpdateLabels(labels)

            coVerify { labelApiMock.insertOrUpdateLabels(any()) }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { labelApiMock.insertOrUpdateLabels(any()) } throws Exception()

            assertThrows<Exception> { labelNetworkDataSource.insertOrUpdateLabels(labels) }
        }

        @Test
        fun `when labels are empty`() = runTest {
            labelNetworkDataSource.insertOrUpdateLabels(listOf())

            coVerify(inverse = true) { labelApiMock.insertOrUpdateLabels(any()) }
        }
    }

    @Nested
    inner class DeleteLabels {
        private lateinit var labelIds: List<String>

        @BeforeEach
        fun setUp() {
            val labels = LabelFactory(dateUtil).createLabels(5)
            labelIds = labels.map { it.id }
        }

        @Test
        fun `should delete labels`() = runTest {
            coEvery { labelApiMock.deleteLabels(any()) } returns Unit

            labelNetworkDataSource.deleteLabels(labelIds)

            coVerify { labelApiMock.deleteLabels(any()) }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { labelApiMock.deleteLabels(any()) } throws Exception()

            assertThrows<Exception> { labelNetworkDataSource.deleteLabels(labelIds) }
        }

        @Test
        fun `when labels are empty`() = runTest {
            labelNetworkDataSource.deleteLabels(listOf())

            coVerify(inverse = true) { labelApiMock.deleteLabels(any()) }
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
            val labelDto = mapper.fromModel(label)
            coEvery { labelApiMock.getLabel(labelId) } returns labelDto

            val result = labelNetworkDataSource.getLabel(labelId)

            assert(result == label)
            coVerify { labelApiMock.getLabel(labelId) }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { labelApiMock.getLabel(labelId) } throws Exception()

            assertThrows<Exception> { labelNetworkDataSource.getLabel(labelId) }
        }

        @Test
        fun `when label does not exist`() = runTest {
            coEvery { labelApiMock.getLabel(labelId) } returns null

            val result = labelNetworkDataSource.getLabel(labelId)

            assert(result == null)
            coVerify { labelApiMock.getLabel(labelId) }
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
            val labelDtoList = labels.map { mapper.fromModel(it) }
            coEvery { labelApiMock.getAllLabels() } returns labelDtoList

            val result = labelNetworkDataSource.getAllLabels()

            assert(result == labels)
            coVerify { labelApiMock.getAllLabels() }
        }

        @Test
        fun `when api call fails`() = runTest {
            coEvery { labelApiMock.getAllLabels() } throws Exception()

            assertThrows<Exception> { labelNetworkDataSource.getAllLabels() }
        }
    }
}
package com.example.note.cache.database

import com.example.note.cache.database.dataSource.LabelCacheDataSource
import com.example.note.mock.MockSetup
import com.example.note.model.Label
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
1. confirm database label empty to start (should be test data inserted from CacheTest.kt)
2. insert a new label, CBS
3. insert a list of labels, CBS
4. insert 1000 new labels, confirm filtered search query works correctly
5. insert 1000 new labels, confirm db size increased
6. delete new label, confirm deleted
7. delete list of labels, CBS
8. update a label, confirm updated
 **/

@HiltAndroidTest
class LabelDatabaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // system in test
    @Inject
    lateinit var labelCacheDataSource: LabelCacheDataSource

    // dependencies
    @Inject
    lateinit var mockSetup: MockSetup

    @Inject
    lateinit var dateUtil: DateUtil

    @Inject
    lateinit var labelFactory: LabelFactory

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
    fun a_searchLabels_confirmDbNotEmpty() = runBlocking {
        val numLabels = labelCacheDataSource.getAllLabels().size
        assertTrue { numLabels > 0 }
    }

    @Test
    fun insertLabel_CBS() = runBlocking {

        val newLabel = labelFactory.createLabel(name = "labelName")
        labelCacheDataSource.insertLabels(listOf(newLabel))

        val cacheLabels = labelCacheDataSource.getAllLabels()
        assert(cacheLabels.contains(newLabel))
    }

    @Test
    fun insertLabelList_CBS() = runBlocking {

        val labelList = labelFactory.createLabels(10)
        labelCacheDataSource.insertLabels(labelList)

        val cacheLabels = labelCacheDataSource.getAllLabels()
        assertTrue { cacheLabels.containsAll(labelList) }
    }

    @Test
    fun insert1000Labels_confirmNumLabelsInDb() = runBlocking {
        val currentNumLabels = labelCacheDataSource.getAllLabels().size

        // insert 1000 labels
        val labelList = labelFactory.createLabels(1000)
        labelCacheDataSource.insertLabels(labelList)

        val numLabels = labelCacheDataSource.getAllLabels().size
        assertEquals(currentNumLabels + 1000, numLabels)
    }

    @Test
    fun insert1000Labels_searchLabelsByTitle_confirm50ExpectedValues() = runBlocking {

        // insert 1000 labels
        val labelList = labelFactory.createLabels(1000)
        labelCacheDataSource.insertLabels(labelList)

        // query 50 labels by specific title
        repeat(50) {
            val randomIndex = Random.nextInt(0, labelList.size - 1)
            val result = labelCacheDataSource.searchLabels(
                query = labelList[randomIndex].name
            ).first()
            assertEquals(labelList[randomIndex].name, result[0].name)
        }
    }


    @Test
    fun insertLabel_deleteLabel_confirmDeleted() = runBlocking {
        val newLabel = labelFactory.createLabel(name = "labelName")
        labelCacheDataSource.insertLabels(listOf(newLabel))

        var labels = labelCacheDataSource.getAllLabels()
        assert(labels.contains(newLabel))

        labelCacheDataSource.deleteLabels(listOf(newLabel.id))
        labels = labelCacheDataSource.getAllLabels()
        assert(!labels.contains(newLabel))
    }

    @Test
    fun deleteLabelList_confirmDeleted() = runBlocking {
        val labelList: ArrayList<Label> = ArrayList(labelCacheDataSource.getAllLabels())

        // select some random labels for deleting
        val labelsToDelete: ArrayList<Label> = ArrayList()

        // 1st
        var labelToDelete = labelList[Random.nextInt(0, labelList.size - 1) + 1]
        labelList.remove(labelToDelete)
        labelsToDelete.add(labelToDelete)

        // 2nd
        labelToDelete = labelList[Random.nextInt(0, labelList.size - 1) + 1]
        labelList.remove(labelToDelete)
        labelsToDelete.add(labelToDelete)

        // 3rd
        labelToDelete = labelList[Random.nextInt(0, labelList.size - 1) + 1]
        labelList.remove(labelToDelete)
        labelsToDelete.add(labelToDelete)

        // 4th
        labelToDelete = labelList[Random.nextInt(0, labelList.size - 1) + 1]
        labelList.remove(labelToDelete)
        labelsToDelete.add(labelToDelete)

        labelCacheDataSource.deleteLabels(labelsToDelete.map { it.id })

        // confirm they were deleted
        val searchResults = labelCacheDataSource.getAllLabels()
        assertFalse { searchResults.containsAll(labelsToDelete) }
    }

    @Test
    fun insertLabel_updateLabel_confirmUpdated() = runBlocking {
        val newLabel = labelFactory.createLabel(name = "labelName")
        labelCacheDataSource.insertLabels(listOf(newLabel))

        // so update timestamp will be different
        delay(1001)

        val newName = UUID.randomUUID().toString()
        labelCacheDataSource.updateLabels(
            listOf(
                newLabel.copy(
                    name = newName,
                    updatedAt = dateUtil.getCurrentTimestamp()
                )
            )
        )

        val labels = labelCacheDataSource.getAllLabels()

        var foundLabel = false
        for (label in labels) {
            if (label.id == newLabel.id) {
                foundLabel = true
                assertEquals(newLabel.id, label.id)
                assertEquals(newName, label.name)
                assert(newLabel.updatedAt != label.updatedAt)
                assertEquals(newLabel.createdAt, label.createdAt)
                break
            }
        }
        assertTrue { foundLabel }
    }
}
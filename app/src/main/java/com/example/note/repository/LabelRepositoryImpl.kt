package com.example.note.repository

import com.example.note.cache.database.dataSource.LabelCacheDataSource
import com.example.note.model.Label
import com.example.note.network.dataSource.LabelNetworkDataSource
import com.example.note.util.DateUtil
import com.example.note.util.printLogD
import com.example.note.util.safeApiCall
import com.example.note.util.safeCacheCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelRepositoryImpl @Inject constructor(
    private val labelCacheDataSource: LabelCacheDataSource,
    private val labelNetworkDataSource: LabelNetworkDataSource,
    private val dateUtil: DateUtil
) : LabelRepository {

    override suspend fun insertLabels(labels: List<Label>) {
        labelCacheDataSource.insertLabels(labels)
        labelNetworkDataSource.insertOrUpdateLabels(labels)
    }

    override suspend fun updateLabels(labels: List<Label>) {
        val now = dateUtil.getCurrentTimestamp()
        val updatedLabels = labels.map { it.copy(updated_at = now) }
        labelCacheDataSource.updateLabels(updatedLabels)
        labelNetworkDataSource.insertOrUpdateLabels(updatedLabels)
    }

    override suspend fun deleteLabels(labels: List<Label>) {
        val ids = labels.map { it.id }
        labelCacheDataSource.deleteLabels(ids)
        labelNetworkDataSource.deleteLabels(ids)
    }

    override suspend fun getLabel(id: String): Label {
        val result = labelCacheDataSource.getLabel(id)
        return result ?: throw Exception("Label not found")
    }

    override suspend fun getAllLabels(): List<Label> {
        return labelCacheDataSource.getAllLabels()
    }

    override fun searchLabels(query: String): Flow<List<Label>> {
        return labelCacheDataSource.searchLabels(query)
    }

    override suspend fun syncLabels() {
        withContext(IO) {
            val cacheInsert = ArrayList<Label>()
            val cacheUpdate = ArrayList<Label>()
            val networkInsert = ArrayList<Label>()
            val networkUpdate = ArrayList<Label>()

            val networkLabels = labelNetworkDataSource.getAllLabels()
            val cachedLabels = ArrayList(labelCacheDataSource.getAllLabels())

            for (networkLabel in networkLabels) {
                try {
                    when (val cachedLabel = labelCacheDataSource.getLabel(networkLabel.id)) {
                        null -> cacheInsert.add(networkLabel)
                        else -> {
                            cachedLabels.remove(cachedLabel)
                            val cacheUpdatedAt = cachedLabel.updated_at
                            val networkUpdatedAt = networkLabel.updated_at

                            when {
                                networkUpdatedAt > cacheUpdatedAt -> cacheUpdate.add(networkLabel)
                                networkUpdatedAt < cacheUpdatedAt -> networkUpdate.add(cachedLabel)
                            }
                        }
                    }
                } catch (e: Exception) {
                    printLogD("syncLabels", e.message.toString())
                }
            }

            networkInsert.addAll(cachedLabels)

            safeCacheCall(IO) { labelCacheDataSource.insertLabels(cacheInsert) }
            safeCacheCall(IO) { labelCacheDataSource.updateLabels(cacheUpdate) }
            safeApiCall(IO) { labelNetworkDataSource.insertOrUpdateLabels(networkInsert + networkUpdate) }
        }
    }
}
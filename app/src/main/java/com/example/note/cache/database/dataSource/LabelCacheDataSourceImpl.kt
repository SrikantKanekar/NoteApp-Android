package com.example.note.cache.database.dataSource

import com.example.note.cache.database.dao.LabelDao
import com.example.note.cache.database.mapper.LabelEntityMapper
import com.example.note.model.Label
import com.example.note.util.cacheCall
import com.example.note.util.printLogD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelCacheDataSourceImpl @Inject constructor(
    private val labelDao: LabelDao,
    private val mapper: LabelEntityMapper
) : LabelCacheDataSource {

    override suspend fun insertLabel(label: Label) {
        cacheCall(IO) {
            labelDao.insertLabel(
                mapper.fromModel(label)
            )
        }
    }

    override suspend fun insertLabels(labels: List<Label>) {
        when {
            labels.isNotEmpty() -> {
                val result = cacheCall(IO) {
                    labelDao.insertLabels(
                        labels.map { label ->
                            mapper.fromModel(label)
                        }
                    )
                }
                printLogD("insertLabels", "${result?.size} labels inserted")
            }
        }
    }

    override suspend fun updateLabel(label: Label) {
        cacheCall(IO) {
            labelDao.updateLabel(
                mapper.fromModel(label)
            )
        }
    }

    override suspend fun updateLabels(labels: List<Label>) {
        when {
            labels.isNotEmpty() -> {
                val result = cacheCall(IO) {
                    labelDao.updateLabels(
                        labels.map { label ->
                            mapper.fromModel(label)
                        }
                    )
                }
                printLogD("updateLabels", "$result labels updated")
            }
        }
    }

    override suspend fun deleteLabel(id: String) {
        cacheCall(IO) {
            labelDao.deleteLabel(id)
        }
    }

    override suspend fun deleteLabels(ids: List<String>) {
        when {
            ids.isNotEmpty() -> {
                val result = cacheCall(IO) {
                    labelDao.deleteLabels(ids)
                }
                printLogD("deleteLabels", "$result labels deleted")
            }
        }
    }

    override suspend fun getLabel(id: String): Label? {
        return cacheCall(IO) {
            labelDao.getLabel(id)?.let { label ->
                mapper.toModel(label)
            }
        }
    }

    override suspend fun getAllLabels(): List<Label> {
        return cacheCall(IO) {
            labelDao.getAllLabels().map { entity ->
                mapper.toModel(entity)
            }
        } ?: listOf()
    }

    override fun searchLabels(query: String) = labelDao
        .searchLabels(query)
        .map { entities ->
            entities.map { entity ->
                mapper.toModel(entity)
            }
        }
}
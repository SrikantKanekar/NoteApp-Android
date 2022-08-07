package com.example.note.cache.database.dataSource

import com.example.note.cache.database.dao.LabelDao
import com.example.note.cache.database.mapper.LabelEntityMapper
import com.example.note.model.Label
import com.example.note.util.cacheCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
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

    override suspend fun updateLabel(label: Label) {
        cacheCall(IO) {
            labelDao.updateLabel(
                mapper.fromModel(label)
            )
        }
    }

    override suspend fun deleteLabel(id: String) {
        cacheCall(IO) {
            labelDao.deleteLabel(id)
        }
    }

    override suspend fun getLabel(id: String): Label? {
        return cacheCall(IO) {
            labelDao.getLabel(id)?.let { label ->
                mapper.toModel(label)
            }
        }
    }

    override suspend fun getAllLabels(): Flow<List<Label>> {
        return labelDao
            .getAllLabels()
            .map { entities ->
                entities.map { entity ->
                    mapper.toModel(entity)
                }
            }
    }
}
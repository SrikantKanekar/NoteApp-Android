package com.example.note.cache.database.dataSource

import com.example.note.model.Label
import kotlinx.coroutines.flow.Flow

interface LabelCacheDataSource {

    suspend fun insertLabel(label: Label)

    suspend fun updateLabel(label: Label)

    suspend fun deleteLabel(id: String)

    suspend fun getLabel(id: String): Label?

    suspend fun getAllLabels(): Flow<List<Label>>
}

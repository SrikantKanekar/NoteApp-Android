package com.example.note.cache.database.dataSource

import com.example.note.model.Label
import kotlinx.coroutines.flow.Flow

interface LabelCacheDataSource {

    suspend fun insertLabels(labels: List<Label>)

    suspend fun updateLabels(labels: List<Label>)

    suspend fun deleteLabels(ids: List<String>)

    suspend fun getLabel(id: String): Label?

    suspend fun getAllLabels(): List<Label>

    fun searchLabels(query: String): Flow<List<Label>>
}

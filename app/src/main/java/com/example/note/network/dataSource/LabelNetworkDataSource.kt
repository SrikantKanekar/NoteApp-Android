package com.example.note.network.dataSource

import com.example.note.model.Label

interface LabelNetworkDataSource {

    suspend fun insertOrUpdateLabels(labels: List<Label>)

    suspend fun deleteLabels(ids: List<String>)

    suspend fun getLabel(id: String): Label?

    suspend fun getAllLabels(): List<Label>
}
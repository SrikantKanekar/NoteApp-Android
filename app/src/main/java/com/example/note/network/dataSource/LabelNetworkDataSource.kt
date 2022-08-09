package com.example.note.network.dataSource

import com.example.note.model.Label

interface LabelNetworkDataSource {

    suspend fun insertLabel(label: Label)

    suspend fun insertLabels(labels: List<Label>)

    suspend fun updateLabel(label: Label)

    suspend fun updateLabels(labels: List<Label>)

    suspend fun deleteLabel(id: String)

    suspend fun deleteLabels(ids: List<String>)

    suspend fun getLabel(id: String): Label?

    suspend fun getAllLabels(): List<Label>
}
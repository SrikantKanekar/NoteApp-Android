package com.example.note.repository

import com.example.note.model.Label
import kotlinx.coroutines.flow.Flow

interface LabelRepository {

    suspend fun insertLabels(labels: List<Label>)

    suspend fun updateLabels(labels: List<Label>)

    suspend fun deleteLabels(labels: List<Label>)

    suspend fun getLabel(id: String): Label?

    suspend fun getAllLabels(): List<Label>

    fun searchLabels(query: String = ""): Flow<List<Label>>

    suspend fun syncLabels()
}

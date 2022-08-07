package com.example.note.network.dataSource

import com.example.note.model.Label
import com.example.note.network.api.LabelApi
import com.example.note.network.mapper.LabelDtoMapper
import com.example.note.util.apiCall
import com.example.note.util.printServerResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelNetworkDataSourceImpl @Inject constructor(
    private val labelApi: LabelApi,
    private val mapper: LabelDtoMapper
) : LabelNetworkDataSource {

    override suspend fun insertLabel(label: Label) {
        apiCall(Dispatchers.IO) {
            val networkResponse = labelApi.insertLabel(
                mapper.fromModel(label)
            )
            printServerResponse("insertLabel", networkResponse)
        }
    }

    override suspend fun updateLabel(label: Label) {
        apiCall(Dispatchers.IO) {
            val networkResponse = labelApi.updateLabel(label.id)
            printServerResponse("updateLabel", networkResponse)
        }
    }

    override suspend fun deleteLabel(id: String) {
        apiCall(Dispatchers.IO) {
            val networkResponse = labelApi.deleteLabel(id)
            printServerResponse("deleteLabel", networkResponse)
        }
    }

    override suspend fun getLabel(id: String): Label? {
        return apiCall(Dispatchers.IO) {
            val labelDto = labelApi.getLabel(id)
            when (labelDto == null) {
                false -> mapper.toModel(labelDto)
                true -> null
            }
        }
    }

    override suspend fun getAllLabels(): List<Label> {
        return apiCall(Dispatchers.IO) {
            labelApi.getAllLabels().map { labelDto ->
                mapper.toModel(labelDto)
            }
        } ?: listOf()
    }
}
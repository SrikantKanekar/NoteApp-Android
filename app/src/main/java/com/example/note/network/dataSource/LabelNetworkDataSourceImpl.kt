package com.example.note.network.dataSource

import com.example.note.model.Label
import com.example.note.network.api.LabelApi
import com.example.note.network.mapper.LabelDtoMapper
import com.example.note.network.requests.LabelDeleteRequest
import com.example.note.network.requests.LabelInsertOrUpdateRequest
import com.example.note.util.apiCall
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelNetworkDataSourceImpl @Inject constructor(
    private val labelApi: LabelApi,
    private val mapper: LabelDtoMapper
) : LabelNetworkDataSource {

    override suspend fun insertOrUpdateLabels(labels: List<Label>) {
        when {
            labels.isNotEmpty() -> {
                apiCall(Dispatchers.IO) {
                    labelApi.insertOrUpdateLabels(
                        LabelInsertOrUpdateRequest(
                            labels = labels.map { label ->
                                mapper.fromModel(label)
                            }
                        )
                    )
                }
            }
        }
    }

    override suspend fun deleteLabels(ids: List<String>) {
        when {
            ids.isNotEmpty() -> {
                apiCall(Dispatchers.IO) {
                    labelApi.deleteLabels(
                        LabelDeleteRequest(ids)
                    )
                }
            }
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
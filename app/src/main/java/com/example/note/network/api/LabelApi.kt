package com.example.note.network.api

import com.example.note.network.dto.LabelDto
import com.example.note.network.requests.LabelDeleteRequest
import com.example.note.network.requests.LabelInsertOrUpdateRequest
import retrofit2.http.*

interface LabelApi {

    @POST("/labels")
    suspend fun insertOrUpdateLabels(
        @Body request: LabelInsertOrUpdateRequest
    )

    @DELETE("/labels")
    suspend fun deleteLabels(
        @Body request: LabelDeleteRequest
    )

    @GET("/labels/{id}")
    suspend fun getLabel(
        @Path("id") id: String
    ): LabelDto?

    @GET("/labels")
    suspend fun getAllLabels(): List<LabelDto>
}
package com.example.note.network.api

import com.example.note.network.dto.LabelDto
import com.example.note.network.response.SimpleResponse
import retrofit2.http.*

interface LabelApi {

    @POST("/label")
    suspend fun insertLabel(
        @Body label: LabelDto
    ): SimpleResponse

    @PUT("/label/{id}")
    suspend fun updateLabel(
        @Path("id") id: String
    ): SimpleResponse

    @DELETE("/label/{id}")
    suspend fun deleteLabel(
        @Path("id") id: String
    ): SimpleResponse

    @GET("/label/{id}")
    suspend fun getLabel(
        @Path("id") id: String
    ): LabelDto?

    @GET("/label")
    suspend fun getAllLabels(): List<LabelDto>
}
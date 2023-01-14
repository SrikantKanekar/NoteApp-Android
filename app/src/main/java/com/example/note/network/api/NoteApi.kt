package com.example.note.network.api

import com.example.note.network.dto.NoteDto
import com.example.note.network.requests.NoteDeleteRequest
import com.example.note.network.requests.NoteInsertOrUpdateRequest
import retrofit2.http.*

interface NoteApi {

    @POST("/notes")
    suspend fun insertOrUpdateNotes(
        @Body request: NoteInsertOrUpdateRequest
    )

    @DELETE("/notes")
    suspend fun deleteNotes(
        @Body request: NoteDeleteRequest
    )

    @GET("/notes/{id}")
    suspend fun getNote(
        @Path("id") id: String
    ): NoteDto?

    @GET("/notes")
    suspend fun getAllNotes(): List<NoteDto>
}
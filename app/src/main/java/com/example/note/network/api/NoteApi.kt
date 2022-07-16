package com.example.note.network.api

import com.example.note.network.dto.NoteDto
import com.example.note.network.response.SimpleResponse
import retrofit2.http.*

interface NoteApi {

    @POST("/insert-or-update-note")
    suspend fun insertOrUpdateNote(
        @Body note: NoteDto
    ): SimpleResponse

    // TODO
    @POST("/insert-or-update-notes")
    suspend fun insertOrUpdateNotes(
        @Body notes: List<NoteDto>
    ): SimpleResponse

    @GET("/get-note/{id}")
    suspend fun getNote(
        @Path("id") id: String
    ): NoteDto?

    @GET("/get-all-notes")
    suspend fun getAllNotes(): List<NoteDto>

    @DELETE("/delete-note/{id}")
    suspend fun deleteNote(
        @Path("id") id: String
    ): SimpleResponse
}
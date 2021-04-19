package com.example.note.framework.datasource.network.api

import com.example.note.framework.datasource.network.dto.NoteDto
import com.example.note.framework.datasource.network.response.SimpleResponse
import retrofit2.http.*

interface DeletedNotesApi {

    @POST("/insert-deleted-note/{id}")
    suspend fun insertDeletedNote(
        @Path("id") id: String
    ): SimpleResponse

    @POST("/insert-deleted-notes")
    suspend fun insertDeletedNotes(
        @Body notes: List<NoteDto>
    ): SimpleResponse

    @GET("/get-deleted-notes")
    suspend fun getDeletedNotes(): List<NoteDto>

    @DELETE("/delete-deleted-note/{id}")
    suspend fun deleteDeletedNote(
        @Path("id") id: String
    ): SimpleResponse
}
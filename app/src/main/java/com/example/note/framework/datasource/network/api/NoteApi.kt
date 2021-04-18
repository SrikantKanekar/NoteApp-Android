package com.example.note.framework.datasource.network.api

import com.example.note.framework.datasource.network.dto.NoteDto
import com.example.note.framework.datasource.network.response.SimpleResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NoteApi {

    @POST("/insert-or-update-note")
    suspend fun insertOrUpdateNote(
        @Body note: NoteDto
    ): SimpleResponse

    @POST("/insert-or-update-notes")
    suspend fun insertOrUpdateNotes(
        @Body notes: List<NoteDto>
    ): SimpleResponse

    @POST("/search-note")
    suspend fun searchNote(
        @Body note: NoteDto
    ): NoteDto?

    @GET("/get-all-notes")
    suspend fun getAllNotes(): List<NoteDto>

    @POST("/delete-note")
    suspend fun deleteNote(
        @Body id: String
    ): SimpleResponse

    @GET("/delete-all-notes")
    suspend fun deleteAllNotes(): SimpleResponse

    @POST("/insert-deleted-note")
    suspend fun insertDeletedNote(
        @Body id: String
    ): SimpleResponse

    @POST("/insert-deleted-notes")
    suspend fun insertDeletedNotes(
        @Body notes: List<NoteDto>
    ): SimpleResponse

    @GET("/get-deleted-notes")
    suspend fun getDeletedNotes(): List<NoteDto>

    @POST("/delete-deleted-note")
    suspend fun deleteDeletedNote(
        @Body note: NoteDto
    ): SimpleResponse
}
package com.example.note.framework.datasource.network.api

import com.example.note.framework.datasource.network.request.AccountRequest
import com.example.note.framework.datasource.network.response.SimpleResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/register")
    suspend fun register(
        @Body registerRequest: AccountRequest
    ): SimpleResponse

    @POST("/login")
    suspend fun login(
        @Body loginRequest: AccountRequest
    ): SimpleResponse
}
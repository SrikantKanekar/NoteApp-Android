package com.example.note.network.api

import com.example.note.network.requests.LoginRequest
import com.example.note.network.response.SimpleResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/register")
    suspend fun register(
        @Body registerRequest: LoginRequest
    ): SimpleResponse

    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): SimpleResponse
}
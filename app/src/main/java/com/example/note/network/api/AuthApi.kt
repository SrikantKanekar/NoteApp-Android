package com.example.note.network.api

import com.example.note.model.User
import com.example.note.network.requests.LoginRequest
import com.example.note.network.requests.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): User

    @POST("/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): User
}
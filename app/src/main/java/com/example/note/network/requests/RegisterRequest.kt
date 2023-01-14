package com.example.note.network.requests

data class RegisterRequest(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String
)

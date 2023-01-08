package com.example.note.model

data class User(
    val email: String,
    val token: String,
    val username: String,
    val isAdmin: Boolean
)

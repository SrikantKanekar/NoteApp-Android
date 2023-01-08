package com.example.note.network.dto

data class UserDto(
    val email: String,
    val token: String,
    val username: String,
    val isAdmin: Boolean
)

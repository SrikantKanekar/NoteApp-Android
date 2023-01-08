package com.example.note.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Label(
    val id: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String
) : Parcelable

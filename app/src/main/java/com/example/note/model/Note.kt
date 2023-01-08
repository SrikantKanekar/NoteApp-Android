package com.example.note.model

import android.os.Parcelable
import com.example.note.model.enums.NoteState
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: String,
    val title: String,
    val body: String,
    val updatedAt: String,
    val createdAt: String,
    val state: NoteState = NoteState.ACTIVE,
    val pinned: Boolean = false,
    val color: Int = 0,
    val labels: List<String> = listOf()
) : Parcelable
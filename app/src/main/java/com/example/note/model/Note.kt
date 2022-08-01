package com.example.note.model

import android.os.Parcelable
import com.example.note.model.enums.NoteState
import com.example.note.model.enums.NoteState.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: String,
    val title: String,
    val body: String,
    val updated_at: String,
    val created_at: String,
    val state: NoteState = ACTIVE
) : Parcelable
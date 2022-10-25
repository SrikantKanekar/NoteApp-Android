package com.example.note.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteGrid(
    val notes: List<Note>,
    val label: String? = null
): Parcelable

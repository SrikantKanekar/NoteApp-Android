package com.example.note.presentation.ui.noteDetail

import android.os.Parcelable
import com.example.note.model.Note
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteDetailUiState(
    val note: Note? = null,
    val title: String = "",
    val body: String = "",
    val errorMessage: String? = null,
) : Parcelable

val NoteDetailUiState.isLoading: Boolean get() = note == null

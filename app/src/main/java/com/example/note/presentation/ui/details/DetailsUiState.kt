package com.example.note.presentation.ui.details

import android.os.Parcelable
import com.example.note.model.Note
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsUiState(
    val note: Note? = null,
    val title: String = "",
    val body: String = "",
    val errorMessage: String? = null,
) : Parcelable

val DetailsUiState.isLoading: Boolean get() = note == null

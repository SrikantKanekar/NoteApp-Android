package com.example.note.presentation.ui.labels

import android.os.Parcelable
import com.example.note.model.Label
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabelsUiState(
    val labels: List<Label> = listOf(),
    val errorMessage: String? = null,
) : Parcelable

val LabelsUiState.isLoading: Boolean get() = false

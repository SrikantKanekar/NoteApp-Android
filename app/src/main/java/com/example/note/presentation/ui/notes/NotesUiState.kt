package com.example.note.presentation.ui.notes

import android.os.Parcelable
import com.example.note.model.Label
import com.example.note.model.Note
import com.example.note.model.NoteGrid
import com.example.note.model.enums.CardLayoutType
import com.example.note.model.enums.CardLayoutType.STAGGERED
import com.example.note.model.enums.PageState
import com.example.note.model.enums.PageState.NOTE
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotesUiState(
    val noteGrids: List<NoteGrid> = listOf(),
    val selectedNotes: List<Note> = listOf(),
    val cardLayoutType: CardLayoutType = STAGGERED,
    val pageState: PageState = NOTE,
    val isSearch: Boolean = false,
    val query: String = "",
    val labels: List<Label> = listOf(),
    val errorMessage: String? = null
) : Parcelable

val NotesUiState.isSelectMode: Boolean get() = selectedNotes.isNotEmpty()

val NotesUiState.selectCount: Int get() = selectedNotes.size
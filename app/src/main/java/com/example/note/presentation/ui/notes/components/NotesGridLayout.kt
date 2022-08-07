package com.example.note.presentation.ui.notes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.note.model.Note
import com.example.note.model.NoteGrid
import com.example.note.model.enums.CardLayoutType
import com.example.note.presentation.components.StaggeredVerticalGrid
import com.example.note.presentation.ui.notes.NotesUiState
import com.example.note.presentation.ui.notes.isSelectMode

@Composable
fun NotesGridLayout(
    uiState: NotesUiState,
    noteGrid: NoteGrid,
    updateSelectedNotes: (Note) -> Unit,
    navigateToDetail: (String) -> Unit
) {
    if (noteGrid.notes.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            noteGrid.label?.let {
                Text(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                    text = noteGrid.label,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            StaggeredVerticalGrid(
                columns = if (uiState.cardLayoutType == CardLayoutType.STAGGERED) 2 else 1
            ) {
                for (note in noteGrid.notes) {
                    NoteCard(
                        note = note,
                        isSelected = uiState.selectedNotes.contains(note),
                        onClick = {
                            when (uiState.isSelectMode) {
                                true -> updateSelectedNotes(note)
                                false -> navigateToDetail(note.id)
                            }
                        },
                        onLongPress = { updateSelectedNotes(note) }
                    )
                }
            }
        }
    }
}
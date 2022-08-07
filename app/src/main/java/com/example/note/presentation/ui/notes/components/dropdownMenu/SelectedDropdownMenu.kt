package com.example.note.presentation.ui.notes.components.dropdownMenu

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.note.model.enums.NoteState
import com.example.note.model.enums.PageState
import com.example.note.presentation.components.MyDropdownMenuItem
import com.example.note.presentation.components.MyIconButton
import com.example.note.presentation.ui.notes.NotesUiState

@Composable
fun SelectedDropdownMenu(
    uiState: NotesUiState,
    onArchiveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCopyClick: () -> Unit,
    onSendClick: () -> Unit,
    onDeleteForeverClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {

        MyIconButton(
            icon = Icons.Outlined.MoreVert,
            description = "More options",
            onClick = { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(0.dp, (-50).dp),
            onDismissRequest = { expanded = false }
        ) {

            if (uiState.pageState != PageState.DELETED) {
                MyDropdownMenuItem(
                    isSelected = !uiState.selectedNotes.any { it.state != NoteState.ARCHIVED },
                    selectText = "Archive",
                    unselectText = "Unarchive",
                    onClick = {
                        expanded = false
                        onArchiveClick()
                    }
                )

                MyDropdownMenuItem(
                    text = "Delete",
                    onClick = {
                        expanded = false
                        onDeleteClick()
                    }
                )

                if (uiState.selectedNotes.size == 1) {
                    MyDropdownMenuItem(
                        text = "Make a copy",
                        onClick = {
                            expanded = false
                            onCopyClick()
                        }
                    )

                    MyDropdownMenuItem(
                        text = "Send",
                        onClick = {
                            expanded = false
                            onSendClick()
                        }
                    )
                }
            } else {
                MyDropdownMenuItem(
                    text = "Delete forever",
                    onClick = {
                        expanded = false
                        onDeleteForeverClick()
                    }
                )
            }
        }
    }
}
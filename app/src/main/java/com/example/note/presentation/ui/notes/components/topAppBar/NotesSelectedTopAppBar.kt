package com.example.note.presentation.ui.notes.components.topAppBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.note.presentation.components.MyIconButton
import com.example.note.presentation.ui.notes.NotesUiState
import com.example.note.presentation.ui.notes.components.dropdownMenu.SelectedDropdownMenu
import com.example.note.presentation.ui.notes.selectCount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesSelectedTopAppBar(
    uiState: NotesUiState,
    onSelectedCloseClick: () -> Unit,
    onPinClick: () -> Unit,
    onReminderClick: () -> Unit,
    onColorClick: () -> Unit,
    onLabelClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCopyClick: () -> Unit,
    onSendClick: () -> Unit,
    onDeleteForeverClick: () -> Unit,
) {
    SmallTopAppBar(
        navigationIcon = {
            MyIconButton(
                icon = Icons.Outlined.Close,
                description = "Close",
                onClick = onSelectedCloseClick
            )
        },
        title = {
            Text(
                text = uiState.selectCount.toString(),
                style = MaterialTheme.typography.titleMedium
            )
        },
        actions = {
            MyIconButton(
                isSelected = !uiState.selectedNotes.any { !it.pinned },
                filledIcon = Icons.Filled.PushPin,
                filledDescription = "Unpin",
                outlinedIcon = Icons.Outlined.PushPin,
                outlinedDescription = "Pin",
                onClick = onPinClick
            )

            MyIconButton(
                icon = Icons.Outlined.AddAlert,
                description = "Add reminder",
                onClick = onReminderClick
            )

            MyIconButton(
                icon = Icons.Outlined.ColorLens,
                description = "Change color",
                onClick = onColorClick
            )

            MyIconButton(
                icon = Icons.Outlined.Label,
                description = "Add label",
                onClick = onLabelClick
            )

            SelectedDropdownMenu(
                uiState = uiState,
                onArchiveClick = onArchiveClick,
                onDeleteClick = onDeleteClick,
                onCopyClick = onCopyClick,
                onSendClick = onSendClick,
                onDeleteForeverClick = onDeleteForeverClick
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
    )
}
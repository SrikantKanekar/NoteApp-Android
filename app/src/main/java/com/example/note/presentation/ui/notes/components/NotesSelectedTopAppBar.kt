package com.example.note.presentation.ui.notes.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.note.presentation.ui.notes.CardLayoutType
import com.example.note.presentation.ui.notes.CardLayoutType.LIST

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesSelectedTopAppBar(
    selectCount: Int,
    onCloseClick: () -> Unit,
    onPinClick: () -> Unit,
    onReminderClick: () -> Unit,
    onColorClick: () -> Unit,
    onLabelClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCopyClick: () -> Unit,
    onSendClick: () -> Unit,
) {
    SmallTopAppBar(
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close icon"
                )
            }
        },
        title = {
            Text(
                text = selectCount.toString(),
                style = MaterialTheme.typography.titleMedium
            )
        },
        actions = {
            IconButton(onClick = onPinClick) {
                Icon(
                    imageVector = Icons.Outlined.PushPin,
                    contentDescription = "PushPin icon"
                )
            }
            IconButton(onClick = onReminderClick) {
                Icon(
                    imageVector = Icons.Outlined.AddAlert,
                    contentDescription = "AddAlert icon"
                )
            }
            IconButton(onClick = onColorClick) {
                Icon(
                    imageVector = Icons.Outlined.ColorLens,
                    contentDescription = "ColorLens icon"
                )
            }
            IconButton(onClick = onLabelClick) {
                Icon(
                    imageVector = Icons.Outlined.Label,
                    contentDescription = "Label icon"
                )
            }
            NotesDropDownMenu(
                onArchiveClick = onArchiveClick,
                onDeleteClick = onDeleteClick,
                onCopyClick = onCopyClick,
                onSendClick = onSendClick
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
    )
}
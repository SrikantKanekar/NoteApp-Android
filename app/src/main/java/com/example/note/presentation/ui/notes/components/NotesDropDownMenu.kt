package com.example.note.presentation.ui.notes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.note.presentation.components.MyDropdownMenuItem

@Composable
fun NotesDropDownMenu(
    onArchiveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCopyClick: () -> Unit,
    onSendClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {

        IconButton(
            onClick = {
                expanded = true
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "MoreVert icon"
            )
        }

        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(0.dp, (-50).dp),
            onDismissRequest = { expanded = false }
        ) {

            MyDropdownMenuItem(
                text = "Archive",
                onClick = {
                    onArchiveClick()
                    expanded = false
                }
            )

            MyDropdownMenuItem(
                text = "Delete",
                onClick = {
                    onDeleteClick()
                    expanded = false
                }
            )

            MyDropdownMenuItem(
                text = "Make a copy",
                onClick = {
                    onCopyClick()
                    expanded = false
                }
            )

            MyDropdownMenuItem(
                text = "Send",
                onClick = {
                    onSendClick()
                    expanded = false
                }
            )
        }
    }
}
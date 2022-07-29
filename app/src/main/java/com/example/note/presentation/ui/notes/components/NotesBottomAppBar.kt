package com.example.note.presentation.ui.notes.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun NotesBottomAppBar(
    onFloatingActionClick: () -> Unit
) {
    BottomAppBar(
        icons = {
            IconButton(
                onClick = {  }
            ) {
                Icon(imageVector = Icons.Outlined.CheckBox, contentDescription = "check")
            }
            IconButton(
                onClick = {  }
            ) {
                Icon(imageVector = Icons.Outlined.Brush, contentDescription = "Brush")
            }
            IconButton(
                onClick = {  }
            ) {
                Icon(imageVector = Icons.Outlined.MicNone, contentDescription = "MicNone")
            }
            IconButton(
                onClick = {  }
            ) {
                Icon(imageVector = Icons.Outlined.Image, contentDescription = "Image")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFloatingActionClick,
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
                }
            )
        }
    )
}
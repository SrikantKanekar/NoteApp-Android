package com.example.note.presentation.ui.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddAlert
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(
    onBackPressed: () -> Unit,
    onPinClick: () -> Unit,
    onReminderClick: () -> Unit,
    onArchiveClick: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackPressed
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back icon"
                )
            }
        },
        title = {},
        actions = {
            IconButton(
                onClick = onPinClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.PushPin,
                    contentDescription = "PushPin icon"
                )
            }
            IconButton(
                onClick = onReminderClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddAlert,
                    contentDescription = "AddAlert icon"
                )
            }
            IconButton(
                onClick = onArchiveClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.Archive,
                    contentDescription = "Archive icon"
                )
            }
        }
    )
}
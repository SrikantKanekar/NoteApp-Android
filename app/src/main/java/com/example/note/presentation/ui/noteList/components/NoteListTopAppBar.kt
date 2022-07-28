package com.example.note.presentation.ui.noteList.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Splitscreen
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun NoteListTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    openDrawer: () -> Unit
) {
    SmallTopAppBar(
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu icon"
                )
            }
        },
        title = {
            Text(
                text = "Search your notes",
                style = MaterialTheme.typography.titleMedium
            )
        },
        actions = {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Splitscreen,
                    contentDescription = "SplitScreen icon"
                )
            }
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "AccountCircle icon"
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = scrollBehavior
    )
}
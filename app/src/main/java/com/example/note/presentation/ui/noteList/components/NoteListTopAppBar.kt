package com.example.note.presentation.ui.noteList.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.note.presentation.ui.noteList.CardLayoutType
import com.example.note.presentation.ui.noteList.CardLayoutType.LIST
import com.example.note.presentation.ui.noteList.CardLayoutType.STAGGERED

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onDrawerClick: () -> Unit,
    onSearchClick: () -> Unit,
    cardLayoutType: CardLayoutType,
    onCardLayoutChange: (CardLayoutType) -> Unit,
    onUserIconClick: () -> Unit
) {
    SmallTopAppBar(
        modifier = Modifier.clickable { onSearchClick() },
        navigationIcon = {
            IconButton(onClick = onDrawerClick) {
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
            when (cardLayoutType) {
                STAGGERED -> {
                    IconButton(
                        onClick = { onCardLayoutChange(LIST) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ViewAgenda,
                            contentDescription = "ViewAgenda icon"
                        )
                    }
                }
                LIST -> {
                    IconButton(
                        onClick = { onCardLayoutChange(STAGGERED) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.GridView,
                            contentDescription = "GridView icon"
                        )
                    }
                }
            }

            IconButton(onClick = onUserIconClick) {
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
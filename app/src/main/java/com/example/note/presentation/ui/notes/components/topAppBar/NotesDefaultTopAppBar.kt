package com.example.note.presentation.ui.notes.components.topAppBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.example.note.model.enums.CardLayoutType
import com.example.note.model.enums.CardLayoutType.LIST
import com.example.note.model.enums.CardLayoutType.STAGGERED
import com.example.note.presentation.components.MyIconButton
import com.example.note.presentation.ui.notes.NotesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesDefaultTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    uiState: NotesUiState,
    onDrawerClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCardLayoutChange: (CardLayoutType) -> Unit,
    onUserIconClick: () -> Unit
) {
    SmallTopAppBar(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onSearchClick() }
            ),
        navigationIcon = {
            MyIconButton(
                icon = Icons.Filled.Menu,
                description = "Open navigation drawer",
                onClick = onDrawerClick
            )
        },
        title = {
            Text(
                modifier = Modifier.alpha(0.5f),
                text = "Search your notes",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        actions = {
            when (uiState.cardLayoutType) {
                STAGGERED -> {
                    MyIconButton(
                        icon = Icons.Outlined.ViewAgenda,
                        description = "Single-column view",
                        onClick = { onCardLayoutChange(LIST) }
                    )
                }
                LIST -> {
                    MyIconButton(
                        icon = Icons.Outlined.GridView,
                        description = "Multi-column view",
                        onClick = { onCardLayoutChange(STAGGERED) }
                    )
                }
            }

            MyIconButton(
                icon = Icons.Outlined.AccountCircle,
                description = "User",
                onClick = onUserIconClick
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = scrollBehavior
    )
}
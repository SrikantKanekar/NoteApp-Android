package com.example.note.presentation.ui.notes.components.topAppBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import com.example.note.model.enums.PageState
import com.example.note.presentation.components.MyIconButton
import com.example.note.presentation.ui.notes.NotesUiState
import com.example.note.presentation.ui.notes.components.dropdownMenu.DeleteDropdownMenu
import com.example.note.presentation.ui.notes.components.dropdownMenu.LabelDropdownMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesDefaultTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    uiState: NotesUiState,
    onDrawerClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCardLayoutChange: (CardLayoutType) -> Unit,
    onUserIconClick: () -> Unit,
    onRenameLabel: () -> Unit,
    onDeleteLabel: () -> Unit,
    onEmptyRecycleBin: () -> Unit
) {
    SmallTopAppBar(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { if (uiState.pageState == PageState.NOTE) onSearchClick() }
            ),
        navigationIcon = {
            MyIconButton(
                icon = Icons.Filled.Menu,
                description = "Open navigation drawer",
                onClick = onDrawerClick
            )
        },
        title = {
            when (uiState.pageState) {
                PageState.NOTE -> {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        text = "Search your notes",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                PageState.REMINDER -> {
                    Text(text = "Reminders")
                }
                is PageState.LABEL -> {
                    val id = uiState.pageState.id
                    Text(text = uiState.labels.find { it.id == id }?.name ?: "")
                }
                PageState.ARCHIVE -> {
                    Text(text = "Archive")
                }
                PageState.DELETED -> {
                    Text(text = "Deleted")
                }
            }
        },
        actions = {
            if (uiState.pageState != PageState.DELETED) {
                if (uiState.pageState != PageState.NOTE) {
                    MyIconButton(
                        icon = Icons.Filled.Search,
                        description = "Search",
                        onClick = onSearchClick
                    )
                }

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
            }

            if (uiState.pageState == PageState.NOTE) {
                MyIconButton(
                    icon = Icons.Outlined.AccountCircle,
                    description = "User",
                    onClick = onUserIconClick
                )
            }

            if (uiState.pageState is PageState.LABEL) {
                LabelDropdownMenu(
                    onRenameLabel = onRenameLabel,
                    onDeleteLabel = onDeleteLabel,
                )
            }

            if (uiState.pageState is PageState.DELETED) {
                if (uiState.noteGrids.first().notes.isNotEmpty()) {
                    DeleteDropdownMenu(
                        onEmptyRecycleBin = onEmptyRecycleBin
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = scrollBehavior
    )
}
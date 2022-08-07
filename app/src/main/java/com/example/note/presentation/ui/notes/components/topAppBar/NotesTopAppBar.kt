package com.example.note.presentation.ui.notes.components.topAppBar

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.note.model.enums.CardLayoutType
import com.example.note.presentation.ui.notes.NotesUiState
import com.example.note.presentation.ui.notes.isSelectMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
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
    onQueryChange: (String) -> Unit,
    onSearchBackClick: () -> Unit,
    onSearchClearClick: () -> Unit,
    onDrawerClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCardLayoutChange: (CardLayoutType) -> Unit,
    onUserIconClick: () -> Unit
) {
    when {
        uiState.isSelectMode -> {
            NotesSelectedTopAppBar(
                uiState = uiState,
                onSelectedCloseClick = onSelectedCloseClick,
                onPinClick = onPinClick,
                onReminderClick = onReminderClick,
                onColorClick = onColorClick,
                onLabelClick = onLabelClick,
                onArchiveClick = onArchiveClick,
                onDeleteClick = onDeleteClick,
                onCopyClick = onCopyClick,
                onSendClick = onSendClick
            )
        }
        uiState.isSearch -> {
            SearchTopAppBar(
                scrollBehavior = scrollBehavior,
                uiState = uiState,
                onQueryChange = onQueryChange,
                onSearchBackClick = onSearchBackClick,
                onSearchClearClick = onSearchClearClick,
            )
        }
        else -> {
            NotesDefaultTopAppBar(
                scrollBehavior = scrollBehavior,
                uiState = uiState,
                onDrawerClick = onDrawerClick,
                onSearchClick = onSearchClick,
                onCardLayoutChange = onCardLayoutChange,
                onUserIconClick = onUserIconClick
            )
        }
    }
}
package com.example.note.presentation.ui.notes.components.topAppBar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import com.example.note.model.enums.CardLayoutType
import com.example.note.presentation.ui.notes.NotesUiState
import com.example.note.presentation.ui.notes.isSelectMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    uiState: NotesUiState,
    onSelectedCloseClick: () -> Unit,
    onPinClick: () -> Unit,
    onReminderClick: () -> Unit,
    onColorClick: () -> Unit,
    onLabelClick: (String) -> Unit,
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
    onUserIconClick: () -> Unit,
    onRenameLabel: () -> Unit,
    onDeleteLabel: () -> Unit,
    onEmptyRecycleBin: () -> Unit,
    onDeleteForeverClick: () -> Unit,
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
                onSendClick = onSendClick,
                onDeleteForeverClick = onDeleteForeverClick
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
                onUserIconClick = onUserIconClick,
                onRenameLabel = onRenameLabel,
                onDeleteLabel = onDeleteLabel,
                onEmptyRecycleBin = onEmptyRecycleBin
            )
        }
    }
}
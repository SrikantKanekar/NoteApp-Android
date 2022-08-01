package com.example.note.presentation.ui.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.note.presentation.components.MyNavigationDrawer
import com.example.note.presentation.components.StaggeredVerticalGrid
import com.example.note.presentation.ui.notes.CardLayoutType.LIST
import com.example.note.presentation.ui.notes.CardLayoutType.STAGGERED
import com.example.note.presentation.ui.notes.components.NoteCard
import com.example.note.presentation.ui.notes.components.NotesBottomAppBar
import com.example.note.presentation.ui.notes.components.NotesSelectedTopAppBar
import com.example.note.presentation.ui.notes.components.NotesTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    navController: NavHostController,
    navigateToDetail: (String) -> Unit,
    navigateToSearch: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MyNavigationDrawer(
        drawerState = drawerState,
        scope = scope,
        navController = navController
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                if (uiState.isSelectMode) {
                    NotesSelectedTopAppBar(
                        selectCount = uiState.selectCount,
                        onCloseClick = { viewModel.clearSelectedNotes() },
                        onPinClick = { },
                        onReminderClick = { },
                        onColorClick = { },
                        onLabelClick = { },
                        onArchiveClick = { },
                        onDeleteClick = { viewModel.deleteSelectedNotes() },
                        onCopyClick = { },
                        onSendClick = { }
                    )
                } else {
                    NotesTopAppBar(
                        scrollBehavior = scrollBehavior,
                        onDrawerClick = { scope.launch { drawerState.open() } },
                        onSearchClick = navigateToSearch,
                        cardLayoutType = uiState.cardLayoutType,
                        onCardLayoutChange = { viewModel.updateCardLayoutType(it) },
                        onUserIconClick = { }
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackBarHostState) },
            bottomBar = {
                NotesBottomAppBar(
                    onCheckboxClick = { },
                    onBrushClick = { },
                    onMicClick = { },
                    onImageClick = { },
                    onFloatingActionClick = {
                        val newNote = viewModel.createNewNote()
                        viewModel.insertNewNote(newNote)
                        navigateToDetail(newNote.id)
                    }
                )
            },
            content = { paddingValues ->
                when (uiState.cardLayoutType) {
                    STAGGERED -> {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                        ) {
                            StaggeredVerticalGrid(
                                modifier = Modifier.padding(
                                    top = paddingValues.calculateTopPadding(),
                                    bottom = paddingValues.calculateBottomPadding(),
                                    start = 5.dp,
                                    end = 5.dp
                                )
                            ) {
                                for (note in uiState.notes.filter { !it.deleted }) {
                                    NoteCard(
                                        note = note,
                                        isSelected = uiState.selectedNotes.contains(note),
                                        onClick = {
                                            if (uiState.isSelectMode) {
                                                viewModel.updateSelectedNotes(note)
                                            } else {
                                                navigateToDetail(note.id)
                                            }
                                        },
                                        onLongPress = { viewModel.updateSelectedNotes(note) }
                                    )
                                }
                            }
                        }
                    }
                    LIST -> {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 6.dp,
                                end = 6.dp,
                                top = paddingValues.calculateTopPadding(),
                                bottom = paddingValues.calculateBottomPadding(),
                            )
                        ) {
                            items(
                                items = uiState.notes.filter { !it.deleted },
                                key = { it.id }
                            ) { note ->
                                NoteCard(
                                    modifier = Modifier.animateItemPlacement(),
                                    note = note,
                                    isSelected = uiState.selectedNotes.contains(note),
                                    onClick = {
                                        if (uiState.isSelectMode) {
                                            viewModel.updateSelectedNotes(note)
                                        } else {
                                            navigateToDetail(note.id)
                                        }
                                    },
                                    onLongPress = { viewModel.updateSelectedNotes(note) }
                                )
                            }
                        }
                    }
                }
            }
        )
    }

    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackBarHostState.showSnackbar(errorMessage)
            viewModel.errorMessageShown()
        }
    }
}
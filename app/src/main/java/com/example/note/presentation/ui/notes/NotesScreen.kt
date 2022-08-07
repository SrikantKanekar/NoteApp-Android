package com.example.note.presentation.ui.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.note.model.enums.PageState
import com.example.note.presentation.components.MyNavigationDrawer
import com.example.note.presentation.ui.notes.components.NotesBottomAppBar
import com.example.note.presentation.ui.notes.components.NotesGridLayout
import com.example.note.presentation.ui.notes.components.topAppBar.NotesTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    navigateToDetail: (String) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToHelp: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MyNavigationDrawer(
        drawerState = drawerState,
        scope = scope,
        navigateToNotes = { viewModel.updatePageState(PageState.NOTE) },
        navigateToReminders = { viewModel.updatePageState(PageState.REMINDER) },
        navigateToLabels = { viewModel.updatePageState(PageState.LABEL(it)) },
        navigateToArchive = { viewModel.updatePageState(PageState.ARCHIVE) },
        navigateToDeleted = { viewModel.updatePageState(PageState.DELETED) },
        navigateToSettings = navigateToSettings,
        navigateToHelp = navigateToHelp
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                NotesTopAppBar(
                    scrollBehavior = scrollBehavior,
                    uiState = uiState,
                    onSelectedCloseClick = { viewModel.clearSelectedNotes() },
                    onPinClick = { viewModel.pinOrUnpinSelectedNotes() },
                    onReminderClick = { },
                    onColorClick = { },
                    onLabelClick = { },
                    onArchiveClick = { viewModel.achieveSelectedNotes() },
                    onDeleteClick = { viewModel.deleteSelectedNotes() },
                    onCopyClick = { },
                    onSendClick = { },
                    onQueryChange = { viewModel.onQueryChange(it) },
                    onSearchBackClick = { viewModel.onSearchBackClick() },
                    onSearchClearClick = { viewModel.onSearchClearClick() },
                    onDrawerClick = { scope.launch { drawerState.open() } },
                    onSearchClick = { viewModel.setSearchMode() },
                    onCardLayoutChange = { viewModel.updateCardLayoutType(it) },
                    onUserIconClick = { }
                )
            },
            snackbarHost = { SnackbarHost(snackBarHostState) },
            bottomBar = {
                if (!uiState.isSearch) {
                    NotesBottomAppBar(
                        onCheckboxClick = { },
                        onBrushClick = { },
                        onMicClick = { },
                        onImageClick = { },
                        onFloatingActionClick = {
                            val newNote = viewModel.createNewNote()
                            viewModel.insertNote(newNote)
                            navigateToDetail(newNote.id)
                        }
                    )
                }
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding(),
                            start = 5.dp,
                            end = 5.dp
                        ),
                ) {

                    for (noteGrid in uiState.noteGrids) {
                        NotesGridLayout(
                            uiState = uiState,
                            noteGrid = noteGrid,
                            updateSelectedNotes = { viewModel.updateSelectedNotes(it) },
                            navigateToDetail = { navigateToDetail(it) }
                        )
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
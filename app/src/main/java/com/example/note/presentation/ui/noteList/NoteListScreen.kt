package com.example.note.presentation.ui.noteList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.note.presentation.components.MyNavigationDrawer
import com.example.note.presentation.components.StaggeredVerticalGrid
import com.example.note.presentation.ui.noteList.components.NoteCard
import com.example.note.presentation.ui.noteList.components.NoteListBottomAppBar
import com.example.note.presentation.ui.noteList.components.NoteListTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel,
    navigateToNoteDetail: (String) -> Unit,
    navigateToSettings: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsState()
    val noteList = viewModel.noteListFlow.collectAsState(listOf())

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarScrollState())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MyNavigationDrawer(
        drawerState = drawerState,
        scope = scope,
        navigateToSettings = navigateToSettings
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                NoteListTopAppBar(
                    scrollBehavior = scrollBehavior,
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            },
            snackbarHost = { SnackbarHost(snackBarHostState) },
            bottomBar = {
                NoteListBottomAppBar(
                    onFloatingActionClick = {
                        val newNote = viewModel.createNewNote()
                        viewModel.insertNewNote(newNote)
                        navigateToNoteDetail(newNote.id)
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    StaggeredVerticalGrid(
                        modifier = Modifier.padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding(),
                            start = 5.dp,
                            end = 5.dp
                        )
                    ) {
                        for (note in noteList.value.filter { !it.deleted }) {
                            NoteCard(
                                note = note,
                                onClick = { navigateToNoteDetail(note.id) }
                            )
                        }
                    }
                }
            }
        )
    }

    uiState.value.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackBarHostState.showSnackbar(errorMessage)
            viewModel.errorMessageShown()
        }
    }
}
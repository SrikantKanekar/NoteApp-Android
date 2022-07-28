package com.example.note.presentation.ui.noteDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.note.presentation.ui.noteDetail.components.NoteBody
import com.example.note.presentation.ui.noteDetail.components.NoteDetailBottomAppBar
import com.example.note.presentation.ui.noteDetail.components.NoteDetailTopAppBar
import com.example.note.presentation.ui.noteDetail.components.NoteTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: String,
    navigateBack: () -> Unit
) {
    val viewModel = hiltViewModel<NoteDetailViewModel>()
    val uiState = viewModel.uiState
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.getNote(noteId)
    }

    Scaffold(
        topBar = {
            NoteDetailTopAppBar(
                onBackPressed = {
                    navigateBack()
                    viewModel.updateNote()
                },
                onPinClick = {
                    navigateBack()
                    viewModel.deleteNote()
                },
                onReminderClick = {},
                onArchiveClick = {}
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = { NoteDetailBottomAppBar() },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(horizontal = 22.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {

                val focusRequester = FocusRequester.Default

                NoteTitle(
                    value = uiState.title,
                    onValueChange = { title ->
                        viewModel.updateNoteTitle(title)
                    },
                    focusRequester = focusRequester
                )

                NoteBody(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = uiState.body,
                    onValueChange = { body ->
                        viewModel.updateNoteBody(body)
                    }
                )
            }
        }
    )

    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackBarHostState.showSnackbar(errorMessage)
            viewModel.errorMessageShown()
        }
    }

    BackHandler {
        navigateBack()
        viewModel.updateNote()
    }
}
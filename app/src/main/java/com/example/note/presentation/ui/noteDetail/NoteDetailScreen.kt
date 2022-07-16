package com.example.note.presentation.ui.noteDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.note.SettingPreferences
import com.example.note.presentation.components.NoteBody
import com.example.note.presentation.components.NoteTitle
import com.example.note.presentation.theme.AppTheme

@Composable
fun NoteDetailScreen(
    theme: SettingPreferences.Theme,
    noteId: String,
    navController: NavHostController
) {
    val viewModel = hiltViewModel<NoteDetailViewModel>()
    val uiState = viewModel.uiState
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        viewModel.getNote(noteId)
    }

    AppTheme(
        theme = theme,
    ) {

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                viewModel.updateNote()
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = {},
                    actions = {
                        uiState.note?.let { note ->
                            IconButton(
                                onClick = {
                                    viewModel.deleteNote(note)
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }
                )
            },
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(25.dp)
                ) {

                    NoteTitle(
                        value = uiState.title,
                        onValueChange = { title ->
                            viewModel.updateNoteTitle(title)
                        }
                    )

                    NoteBody(
                        modifier = Modifier.fillMaxSize(),
                        value = uiState.body,
                        onValueChange = { body ->
                            viewModel.updateNoteBody(body)
                        }
                    )
                }
            }
        )
    }

    BackHandler {
        viewModel.updateNote()
        navController.popBackStack()
    }
}
package com.example.note.framework.presentation.ui.noteDetail

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavHostController
import com.example.faircon.SettingPreferences
import com.example.note.framework.presentation.components.NoteBody
import com.example.note.framework.presentation.components.NoteTitle
import com.example.note.framework.presentation.theme.AppTheme
import com.example.note.framework.presentation.ui.noteDetail.state.NoteDetailStateEvent.*

@Composable
fun NoteDetailScreen(
    theme: SettingPreferences.Theme,
    scaffoldState: ScaffoldState,
    noteId: String,
    navController: NavHostController
) {
    val viewModel = hiltNavGraphViewModel<NoteDetailViewModel>()
    val viewState = viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setStateEvent(GetNoteEvent(noteId))
    }

    AppTheme(
        theme = theme,
        scaffoldState = scaffoldState
    ) {

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                viewModel.setStateEvent(UpdateNoteEvent)
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
                        IconButton(
                            onClick = {
                                viewModel.setStateEvent(DeleteNoteEvent(viewState.value.note!!))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
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
                        value = viewState.value.title,
                        onValueChange = { title ->
                            viewModel.updateNoteTitle(title)
                        }
                    )

                    NoteBody(
                        modifier = Modifier.fillMaxSize(),
                        value = viewState.value.body,
                        onValueChange = { body ->
                            viewModel.updateNoteBody(body)
                        }
                    )
                }
            }
        )
    }

    BackHandler {
        viewModel.setStateEvent(UpdateNoteEvent)
        navController.popBackStack()
    }
}
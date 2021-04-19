package com.example.note.framework.presentation.ui.noteList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.SettingPreferences.Theme
import com.example.note.framework.presentation.components.MySearchView
import com.example.note.framework.presentation.components.SwipeNoteCard
import com.example.note.framework.presentation.navigation.Navigation
import com.example.note.framework.presentation.navigation.Navigation.NoteDetail
import com.example.note.framework.presentation.theme.AppTheme
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.DeleteNoteEvent
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.InsertNewNoteEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@Composable
fun NoteListScreen(
    theme: Theme,
    scaffoldState: ScaffoldState,
    viewModel: NoteListViewModel,
    navController: NavHostController
) {
    AppTheme(
        theme = theme,
        displayProgressBar = viewModel.shouldDisplayProgressBar.value,
        scaffoldState = scaffoldState,
        stateMessage = viewModel.stateMessage.value,
        removeStateMessage = { viewModel.removeStateMessage() }
    ) {

        val noteList = viewModel.noteListFlow.collectAsState(initial = ArrayList())
        val viewState = viewModel.viewState.collectAsState()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(MaterialTheme.shapes.medium),
                    content = {
                        MySearchView(
                            value = viewState.value.searchQuery ?: "",
                            onValueChange = { viewModel.setSearchQuery(it) },
                            onSearch = { }
                        )
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    cutoutShape = CircleShape
                ) {
                    IconButton(onClick = { navController.navigate(Navigation.Settings.route) }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            },
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val newNote = viewModel.createNewNote()
                        viewModel.setStateEvent(InsertNewNoteEvent(newNote))
                        navController.navigate(
                            route = NoteDetail.route + "/${newNote.id}"
                        )
                    },
                    content = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "New Note")
                    }
                )
            },
            isFloatingActionButtonDocked = true,
            content = {

                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 65.dp)
                ) {

                    for (note in noteList.value){
                        item {
                            SwipeNoteCard(
                                note = note,
                                onClick = {
                                    navController.navigate(
                                        route = NoteDetail.route + "/$it"
                                    )
                                },
                                dismissedToStart = {
                                    viewModel.setStateEvent(DeleteNoteEvent(it))
                                },
                                dismissedToEnd = {
                                    viewModel.setStateEvent(DeleteNoteEvent(it))
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}
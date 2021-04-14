package com.example.note.framework.presentation.ui.noteList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.SettingPreferences.Theme
import com.example.note.framework.presentation.navigation.Navigation
import com.example.note.framework.presentation.navigation.Navigation.NoteDetail
import com.example.note.framework.presentation.theme.AppTheme
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.InsertNewNoteEvent
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.SearchNotesEvent

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

        val viewState = viewModel.viewState.collectAsState()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(MaterialTheme.shapes.medium),
                    content = {
                        BasicTextField(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            value = viewState.value.searchQuery ?: "",
                            onValueChange = { viewModel.setSearchQuery(it) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = { viewModel.setStateEvent(SearchNotesEvent) }
                            ),
                            singleLine = true,
                            textStyle = TextStyle(
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 15.sp
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colors.onSurface)
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
                        viewModel.setStateEvent(
                            InsertNewNoteEvent(title = "new note")
                        )
                    },
                    content = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "New Note")
                    }
                )
            },
            isFloatingActionButtonDocked = true,
            content = {

                val noteList = viewState.value.noteList

                if (noteList != null) {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 65.dp)
                    ) {
                        items(noteList.size) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .padding(8.dp)
                                    .clickable {
                                        navController.navigate(NoteDetail.route)
                                    },
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(text = noteList[index].title)
                            }
                        }
                    }
                }
            }
        )
    }
}
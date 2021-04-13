package com.example.note.framework.presentation.ui.noteList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.note.business.domain.model.Note
import com.example.note.framework.presentation.theme.NoteTheme
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.*

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel,
    navController: NavHostController
) {
    NoteTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.setStateEvent(GetNumNotesInCacheEvent) }) {
                Text(text = "Get count")
            }
            Button(onClick = { viewModel.setStateEvent(InsertNewNoteEvent("title")) }) {
                Text(text = "Insert")
            }
            Button(onClick = {
                viewModel.setStateEvent(
                    DeleteNoteEvent(
                        Note(
                            "title",
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                )
            }) {
                Text(text = "Get count")
            }
        }
    }
}
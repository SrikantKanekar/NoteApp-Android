package com.example.note.framework.presentation.ui.noteDetail

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.faircon.SettingPreferences
import com.example.note.framework.presentation.theme.AppTheme

@Composable
fun NoteDetailScreen(
    theme: SettingPreferences.Theme,
    scaffoldState: ScaffoldState,
    navController: NavHostController
) {
    AppTheme(
        theme = theme,
        scaffoldState = scaffoldState
    ) {

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = {},
                    actions = {
                        IconButton(onClick = {  }) {
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
                Text(text = "Title")
            }
        )
    }
}
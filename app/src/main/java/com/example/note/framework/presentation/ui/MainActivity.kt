package com.example.note.framework.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.note.framework.presentation.navigation.Navigation.*
import com.example.note.framework.presentation.ui.noteList.NoteListScreen
import com.example.note.framework.presentation.ui.noteList.NoteListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            NavHost(
                navController = navController,
                startDestination = NoteList.route
            ) {

                composable(route = Splash.route) {
//                    SplashScreen(
//                        theme = appTheme.value,
//                        scaffoldState = scaffoldState,
//                        viewModel = connectViewModel,
//                        navController = navController
//                    )
                }

                composable(route = NoteList.route) { backStackEntry ->
                    val noteListViewModel = hiltNavGraphViewModel<NoteListViewModel>(backStackEntry)
                    NoteListScreen(
//                        theme = appTheme.value,
//                        scaffoldState = scaffoldState,
                        viewModel = noteListViewModel,
                        navController = navController
                    )
                }

                composable(route = NoteDetail.route) {
//                    ModeScreen(
//                        theme = appTheme.value,
//                        scaffoldState = scaffoldState,
//                        viewModel = modeViewModel,
//                        navController = navController
//                    )
                }

                composable(route = Settings.route) {
//                    SettingScreen(
//                        theme = appTheme.value,
//                        scaffoldState = scaffoldState,
//                        viewModel = modeViewModel,
//                        navController = navController
//                    )
                }
            }
        }
    }
}
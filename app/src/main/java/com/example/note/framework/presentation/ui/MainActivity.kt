package com.example.note.framework.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.rememberScaffoldState
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.note.framework.presentation.navigation.Navigation.*
import com.example.note.framework.presentation.ui.noteDetail.NoteDetailScreen
import com.example.note.framework.presentation.ui.noteList.NoteListScreen
import com.example.note.framework.presentation.ui.noteList.NoteListViewModel
import com.example.note.framework.presentation.ui.settings.SettingScreen
import com.example.note.framework.presentation.ui.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            NavHost(
                navController = navController,
                startDestination = Splash.route
            ) {

                composable(route = Splash.route) {
                    SplashScreen(
                        theme = appTheme.value,
                        navController = navController
                    )
                }

                composable(route = NoteList.route) { backStackEntry ->
                    val noteListViewModel = hiltNavGraphViewModel<NoteListViewModel>(backStackEntry)
                    NoteListScreen(
                        theme = appTheme.value,
                        scaffoldState = scaffoldState,
                        viewModel = noteListViewModel,
                        navController = navController
                    )
                }

                composable(
                    route = NoteDetail.route + "/{NoteId}",
                    arguments = listOf(navArgument("NoteId"){
                        type = NavType.StringType
                    })
                ) {
                    NoteDetailScreen(
                        theme = appTheme.value,
                        scaffoldState = scaffoldState,
                        noteId = it.arguments?.getString("NoteId") ?: "",
                        navController = navController
                    )
                }

                composable(route = Settings.route) {
                    SettingScreen(
                        theme = appTheme.value,
                        scaffoldState = scaffoldState
                    )
                }
            }
        }
    }
}
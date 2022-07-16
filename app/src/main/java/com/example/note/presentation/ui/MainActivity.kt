package com.example.note.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.note.SettingPreferences
import com.example.note.cache.dataStore.SettingDataStore
import com.example.note.presentation.navigation.Navigation.*
import com.example.note.presentation.ui.noteDetail.NoteDetailScreen
import com.example.note.presentation.ui.noteList.NoteListScreen
import com.example.note.presentation.ui.noteList.NoteListViewModel
import com.example.note.presentation.ui.settings.SettingScreen
import com.example.note.presentation.ui.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var settingDataStore: SettingDataStore

    private val appTheme = mutableStateOf(SettingPreferences.Theme.DARK)

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            settingDataStore.settingFlow.collect { setting ->
                appTheme.value = setting.theme
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

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
                    val noteListViewModel = hiltViewModel<NoteListViewModel>(backStackEntry)
                    NoteListScreen(
                        theme = appTheme.value,
                        viewModel = noteListViewModel,
                        navController = navController
                    )
                }

                composable(
                    route = NoteDetail.route + "/{NoteId}",
                    arguments = listOf(navArgument("NoteId") {
                        type = NavType.StringType
                    })
                ) {
                    NoteDetailScreen(
                        theme = appTheme.value,
                        noteId = it.arguments?.getString("NoteId") ?: "",
                        navController = navController
                    )
                }

                composable(route = Settings.route) {
                    SettingScreen(
                        theme = appTheme.value,
                    )
                }
            }
        }
    }
}
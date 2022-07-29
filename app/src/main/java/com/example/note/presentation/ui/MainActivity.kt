package com.example.note.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.note.SettingPreferences.Theme
import com.example.note.cache.dataStore.SettingDataStore
import com.example.note.presentation.navigation.Navigation.*
import com.example.note.presentation.theme.AppTheme
import com.example.note.presentation.ui.details.DetailsScreen
import com.example.note.presentation.ui.helpAndFeedback.HelpAndFeedbackScreen
import com.example.note.presentation.ui.label.LabelScreen
import com.example.note.presentation.ui.notes.NotesScreen
import com.example.note.presentation.ui.notes.NotesViewModel
import com.example.note.presentation.ui.search.SearchScreen
import com.example.note.presentation.ui.settings.SettingScreen
import com.example.note.presentation.ui.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingDataStore: SettingDataStore

    private val appTheme = mutableStateOf(Theme.DARK)

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

            val splashNavController = rememberNavController()

            NavHost(
                navController = splashNavController,
                startDestination = Splash.route
            ) {
                composable(route = Splash.route) {
                    SplashScreen(navController = splashNavController)
                }

                composable(route = Main.route) {
                    AppTheme(appTheme.value) {
                        Surface(color = MaterialTheme.colorScheme.background) {

                            val mainNavController = rememberNavController()

                            NavHost(
                                navController = mainNavController,
                                startDestination = Notes.route
                            ) {

                                composable(route = Notes.route) { backStackEntry ->
                                    val viewModel = hiltViewModel<NotesViewModel>(backStackEntry)
                                    NotesScreen(
                                        viewModel = viewModel,
                                        navigateToDetail = { id ->
                                            mainNavController.navigate(
                                                route = Details.route + "/$id"
                                            )
                                        },
                                        navigateToSearch = {
                                            mainNavController.navigate(Search.route)
                                        },
                                        navController = mainNavController
                                    )
                                }

                                composable(
                                    route = Details.route + "/{noteId}",
                                    arguments = listOf(
                                        navArgument("noteId") {
                                            type = NavType.StringType
                                        }
                                    )
                                ) {
                                    DetailsScreen(
                                        noteId = it.arguments?.getString("noteId") ?: "",
                                        navigateBack = { mainNavController.popBackStack() }
                                    )
                                }

                                composable(route = Search.route) {
                                    SearchScreen()
                                }

                                composable(route = Settings.route) {
                                    SettingScreen()
                                }

                                composable(route = HelpAndFeedback.route) {
                                    HelpAndFeedbackScreen()
                                }

                                composable(route = Label.route) {
                                    LabelScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
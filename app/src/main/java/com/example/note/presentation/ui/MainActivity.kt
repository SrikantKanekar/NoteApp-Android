package com.example.note.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.note.SettingPreferences.Theme
import com.example.note.cache.dataStore.SettingDataStore
import com.example.note.model.enums.LabelScreenMode
import com.example.note.presentation.navigation.Navigation.*
import com.example.note.presentation.theme.AppTheme
import com.example.note.presentation.ui.details.DetailsScreen
import com.example.note.presentation.ui.helpAndFeedback.HelpAndFeedbackScreen
import com.example.note.presentation.ui.labels.LabelsScreen
import com.example.note.presentation.ui.notes.NotesScreen
import com.example.note.presentation.ui.notes.NotesViewModel
import com.example.note.presentation.ui.settings.SettingScreen
import com.example.note.presentation.ui.splash.SplashScreen
import com.example.note.util.CREATE_LABELS_ACTION
import com.example.note.util.EDIT_LABELS_ACTION
import com.example.note.util.SELECT_LABELS_ACTION
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
                                            mainNavController.navigate(Details.route + "/$id")
                                        },
                                        navigateToLabelsScreen = {
                                            mainNavController.navigate(Label.route + "/$it")
                                        },
                                        navigateToSettings = {
                                            mainNavController.navigate(Settings.route)
                                        },
                                        navigateToHelp = {
                                            mainNavController.navigate(HelpAndFeedback.route)
                                        }
                                    )
                                }

                                composable(
                                    route = Details.route + "/{noteId}",
                                ) {
                                    DetailsScreen(
                                        noteId = it.arguments?.getString("noteId") ?: "",
                                        navigateBack = { mainNavController.popBackStack() }
                                    )
                                }

                                composable(
                                    route = Label.route + "/{action}?noteIds={noteIds}",
                                    arguments = listOf(navArgument("noteIds") { defaultValue = "" })
                                ) {
                                    val action = it.arguments?.getString("action") ?: ""
                                    val noteIds = it.arguments?.getString("noteIds") ?: ""
                                    val mode = getLabelScreenMode(
                                        action = action,
                                        noteIds = noteIds
                                    )
                                    LabelsScreen(mode = mode)
                                }

                                composable(route = Settings.route) {
                                    SettingScreen()
                                }

                                composable(route = HelpAndFeedback.route) {
                                    HelpAndFeedbackScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getLabelScreenMode(action: String, noteIds: String): LabelScreenMode {
        return when (action) {
            CREATE_LABELS_ACTION -> LabelScreenMode.CREATE
            EDIT_LABELS_ACTION -> LabelScreenMode.EDIT
            SELECT_LABELS_ACTION -> {
                LabelScreenMode.SELECT(noteIds.split(", "))
            }
            else -> LabelScreenMode.EDIT
        }
    }
}
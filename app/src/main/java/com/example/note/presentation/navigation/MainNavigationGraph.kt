package com.example.note.presentation.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.note.SettingPreferences.Theme
import com.example.note.model.enums.LabelScreenMode
import com.example.note.presentation.theme.AppTheme
import com.example.note.presentation.ui.details.DetailsScreen
import com.example.note.presentation.ui.helpAndFeedback.HelpAndFeedbackScreen
import com.example.note.presentation.ui.labels.LabelsScreen
import com.example.note.presentation.ui.notes.NotesScreen
import com.example.note.presentation.ui.notes.NotesViewModel
import com.example.note.presentation.ui.settings.SettingScreen
import com.example.note.util.CREATE_LABELS_ACTION
import com.example.note.util.EDIT_LABELS_ACTION
import com.example.note.util.SELECT_LABELS_ACTION

@Composable
fun MainNavigationGraph(
    theme: Theme
) {
    AppTheme(theme) {
        Surface(color = MaterialTheme.colorScheme.background) {

            val mainNavController = rememberNavController()

            NavHost(
                navController = mainNavController,
                startDestination = NavigationRoute.Notes.route
            ) {

                composable(route = NavigationRoute.Notes.route) { backStackEntry ->
                    val viewModel = hiltViewModel<NotesViewModel>(backStackEntry)
                    NotesScreen(
                        viewModel = viewModel,
                        navigateToDetail = { id ->
                            mainNavController.navigate(NavigationRoute.Details.route + "/$id")
                        },
                        navigateToLabelsScreen = {
                            mainNavController.navigate(NavigationRoute.Label.route + "/$it")
                        },
                        navigateToSettings = {
                            mainNavController.navigate(NavigationRoute.Settings.route)
                        },
                        navigateToHelp = {
                            mainNavController.navigate(NavigationRoute.HelpAndFeedback.route)
                        }
                    )
                }

                composable(
                    route = NavigationRoute.Details.route + "/{noteId}",
                ) {
                    DetailsScreen(
                        noteId = it.arguments?.getString("noteId") ?: "",
                        navigateBack = { mainNavController.popBackStack() }
                    )
                }

                composable(
                    route = NavigationRoute.Label.route + "/{action}?noteIds={noteIds}",
                    arguments = listOf(navArgument("noteIds") { defaultValue = "" })
                ) {
                    val action = it.arguments?.getString("action") ?: ""
                    val noteIds = it.arguments?.getString("noteIds") ?: ""
                    val mode = getLabelScreenMode(
                        action = action,
                        noteIds = noteIds
                    )
                    LabelsScreen(
                        mode = mode,
                        navigateBack = { mainNavController.popBackStack() }
                    )
                }

                composable(route = NavigationRoute.Settings.route) {
                    SettingScreen()
                }

                composable(route = NavigationRoute.HelpAndFeedback.route) {
                    HelpAndFeedbackScreen()
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
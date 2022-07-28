package com.example.note.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.note.SettingPreferences.Theme
import com.example.note.mock.MockSetup
import com.example.note.presentation.navigation.Navigation.*
import com.example.note.presentation.theme.AppTheme
import com.example.note.presentation.ui.noteDetail.NoteDetailScreen
import com.example.note.presentation.ui.noteList.NoteListScreen
import com.example.note.presentation.ui.noteList.NoteListViewModel
import com.example.note.presentation.ui.settings.SettingScreen
import com.example.note.presentation.ui.splash.SplashScreen
import com.example.note.util.route
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class EndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var mockSetup: MockSetup

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        mockSetup.init()
        composeRule.setContent {
            val splashNavController = rememberNavController()

            NavHost(
                navController = splashNavController,
                startDestination = Splash.route
            ) {
                composable(route = Splash.route) {
                    SplashScreen(navController = splashNavController)
                }

                composable(route = Main.route) {
                    AppTheme(Theme.DARK) {
                        Surface(color = MaterialTheme.colorScheme.background) {

                            val mainNavController = rememberNavController()

                            NavHost(
                                navController = mainNavController,
                                startDestination = NoteList.route
                            ) {

                                composable(route = NoteList.route) { backStackEntry ->
                                    val viewModel = hiltViewModel<NoteListViewModel>(backStackEntry)
                                    NoteListScreen(
                                        viewModel = viewModel,
                                        navigateToNoteDetail = { id ->
                                            mainNavController.navigate(
                                                route = NoteDetail.route + "/$id"
                                            )
                                        },
                                        navigateToSettings = {
                                            mainNavController.navigate(Settings.route)
                                        }
                                    )
                                }

                                composable(
                                    route = NoteDetail.route + "/{noteId}",
                                    arguments = listOf(
                                        navArgument("noteId") {
                                            type = NavType.StringType
                                        }
                                    )
                                ) {
                                    NoteDetailScreen(
                                        noteId = it.arguments?.getString("noteId") ?: "",
                                        navigateBack = { mainNavController.popBackStack() }
                                    )
                                }

                                composable(route = Settings.route) {
                                    SettingScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun endToEndTest() {

        // assert splash screen displayed
        assertEquals(navController.route, Splash.route)
        composeRule.waitUntil(10000) { navController.route == NoteList.route }

        // create new note
        composeRule.onNodeWithContentDescription("Add note").performClick()

        // enter title, content
        composeRule.onNodeWithContentDescription("Note title")
            .performTextReplacement("New note title")
        composeRule.onNodeWithContentDescription("Note body")
            .performTextReplacement("New note body")

        // press back arrow
        composeRule.onNodeWithContentDescription("Back icon").performClick()

        // search title, assert it exist, open
        composeRule.onNodeWithContentDescription("Search view")
            .performTextReplacement("New note title")
        composeRule.onNodeWithContentDescription("Note card")
            .assertTextContains("New note title")
            .performClick()

        // edit title and content, go back
        composeRule.onNodeWithContentDescription("Note title")
            .performTextReplacement("New note title modified")
        composeRule.onNodeWithContentDescription("Note body")
            .performTextReplacement("New note body modified")
        composeRule.onNodeWithContentDescription("Back icon").performClick()

        // assert modified, open
        composeRule.onNodeWithContentDescription("Note card")
            .assertTextContains("New note title modified")
            .performClick()

        // delete, assert no note in list
        composeRule.onNodeWithContentDescription("Delete icon").performClick()
        composeRule.onNodeWithContentDescription("Note card").assertDoesNotExist()

        // open settings, toggle, assert
        composeRule.onNodeWithContentDescription("Settings").performClick()
        composeRule.onNodeWithContentDescription("Theme switch")
            .assertIsOn()
            .performClick()
        composeRule.onNodeWithText("light").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Theme switch")
            .assertIsOff()
            .performClick()
        composeRule.onNodeWithText("dark").assertIsDisplayed()
    }
}
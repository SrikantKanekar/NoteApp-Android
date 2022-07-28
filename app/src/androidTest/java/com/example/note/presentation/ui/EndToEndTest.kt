package com.example.note.presentation.ui

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
            navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Splash.route
            ) {

                composable(route = Splash.route) {
                    SplashScreen(navController = navController)
                }

                composable(route = NoteList.route) { backStackEntry ->
                    val noteListViewModel = hiltViewModel<NoteListViewModel>(backStackEntry)
                    NoteListScreen(
                        theme = Theme.DARK,
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
                        theme = Theme.DARK,
                        noteId = it.arguments?.getString("NoteId") ?: "",
                        navController = navController
                    )
                }

                composable(route = Settings.route) {
                    SettingScreen(
                        theme = Theme.DARK,
                    )
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
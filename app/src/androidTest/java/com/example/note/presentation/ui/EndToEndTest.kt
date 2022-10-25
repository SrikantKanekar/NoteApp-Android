package com.example.note.presentation.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import com.example.note.SettingPreferences.Theme
import com.example.note.mock.MockSetup
import com.example.note.presentation.navigation.NavigationGraph
import com.example.note.presentation.navigation.NavigationRoute.Notes
import com.example.note.presentation.navigation.NavigationRoute.Splash
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
        composeRule.activity.setContent {
            NavigationGraph(theme = Theme.DARK)
        }
    }

    @Test
    fun endToEndTest() {

//        // assert splash screen displayed
//        assertEquals(navController.route, Splash.route)
//        composeRule.waitUntil(10000) { navController.route == Notes.route }
//
//        // create new note
//        composeRule.onNodeWithContentDescription("Add note").performClick()
//
//        // enter title, content
//        composeRule.onNodeWithContentDescription("Note title")
//            .performTextReplacement("New note title")
//        composeRule.onNodeWithContentDescription("Note body")
//            .performTextReplacement("New note body")
//
//        // press back arrow
//        composeRule.onNodeWithContentDescription("Back icon").performClick()
//
//        // search title, assert it exist, open
//        composeRule.onNodeWithContentDescription("Search view")
//            .performTextReplacement("New note title")
//        composeRule.onNodeWithContentDescription("Note card")
//            .assertTextContains("New note title")
//            .performClick()
//
//        // edit title and content, go back
//        composeRule.onNodeWithContentDescription("Note title")
//            .performTextReplacement("New note title modified")
//        composeRule.onNodeWithContentDescription("Note body")
//            .performTextReplacement("New note body modified")
//        composeRule.onNodeWithContentDescription("Back icon").performClick()
//
//        // assert modified, open
//        composeRule.onNodeWithContentDescription("Note card")
//            .assertTextContains("New note title modified")
//            .performClick()
//
//        // delete, assert no note in list
//        composeRule.onNodeWithContentDescription("Delete icon").performClick()
//        composeRule.onNodeWithContentDescription("Note card").assertDoesNotExist()
//
//        // open settings, toggle, assert
//        composeRule.onNodeWithContentDescription("Settings").performClick()
//        composeRule.onNodeWithContentDescription("Theme switch")
//            .assertIsOn()
//            .performClick()
//        composeRule.onNodeWithText("light").assertIsDisplayed()
//        composeRule.onNodeWithContentDescription("Theme switch")
//            .assertIsOff()
//            .performClick()
//        composeRule.onNodeWithText("dark").assertIsDisplayed()
    }
}
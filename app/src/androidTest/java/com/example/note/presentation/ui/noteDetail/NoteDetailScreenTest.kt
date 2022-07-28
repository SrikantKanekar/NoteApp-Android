package com.example.note.presentation.ui.noteDetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextReplacement
import androidx.navigation.compose.rememberNavController
import com.example.note.SettingPreferences.Theme
import com.example.note.mock.MockSetup
import com.example.note.presentation.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class NoteDetailScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var mockSetup: MockSetup

    @Before
    fun setUp() {
        hiltRule.inject()
        mockSetup.init()
        composeRule.setContent {
            NoteDetailScreen(
                noteId = "2474abaa-788a-4a6b-948z-87a2167hb0ec",
                navigateBack = {}
            )
        }
    }

    @Test
    fun assertIcons_isVisible() {
        composeRule.onNodeWithContentDescription("Back icon").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Delete icon").assertIsDisplayed()
    }

    @Test
    fun assertTitleAndContent_isVisible() {
        composeRule.onNodeWithContentDescription("Note title")
            .assertIsDisplayed()
            .performTextReplacement("Note title modified")

        composeRule.onNodeWithContentDescription("Note title")
            .assertTextEquals("Note title modified")

        composeRule.onNodeWithContentDescription("Note body")
            .assertIsDisplayed()
            .performTextReplacement("Note body modified")

        composeRule.onNodeWithContentDescription("Note body")
            .assertTextEquals("Note body modified")
    }
}
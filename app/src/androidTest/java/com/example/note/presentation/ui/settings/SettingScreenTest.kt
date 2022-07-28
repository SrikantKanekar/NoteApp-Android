package com.example.note.presentation.ui.settings

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.note.SettingPreferences.Theme
import com.example.note.mock.MockSetup
import com.example.note.presentation.theme.AppTheme
import com.example.note.presentation.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SettingScreenTest {

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
            AppTheme(theme = Theme.LIGHT) {
                SettingScreen()
            }
        }
    }

    @Test
    fun assertTexts_areVisible() {
        composeRule.onNodeWithContentDescription("Theme setting icon").assertIsDisplayed()
        composeRule.onNodeWithText("Theme").assertIsDisplayed()
    }

    @Test
    fun assertToggle() {
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
package com.example.note.presentation.ui.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.note.SettingPreferences.Theme
import com.example.note.mock.MockSetup
import com.example.note.presentation.navigation.NavigationRoute.Splash
import com.example.note.presentation.theme.AppTheme
import com.example.note.presentation.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SplashScreenTest {

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
            val navController = rememberNavController()
            AppTheme(theme = Theme.LIGHT) {
                NavHost(
                    navController = navController,
                    startDestination = Splash.route
                ) {
                    composable(route = Splash.route) {
                        SplashScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun assertLogo_isVisible() {
        composeRule.onNodeWithContentDescription("Splash logo").assertIsDisplayed()
    }
}
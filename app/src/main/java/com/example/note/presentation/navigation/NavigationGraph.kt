package com.example.note.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.note.SettingPreferences.Theme
import com.example.note.presentation.ui.splash.SplashScreen

@Composable
fun NavigationGraph(
    theme: Theme
) {
    val splashNavController = rememberNavController()

    NavHost(
        navController = splashNavController,
        startDestination = NavigationRoute.Splash.route
    ) {
        composable(route = NavigationRoute.Splash.route) {
            SplashScreen(navController = splashNavController)
        }

        composable(route = NavigationRoute.Main.route) {
            MainNavigationGraph(theme = theme)
        }
    }
}
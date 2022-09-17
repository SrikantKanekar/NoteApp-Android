package com.example.note.presentation.navigation

sealed class NavigationRoute(
    val route: String
) {
    object Splash : NavigationRoute("Splash")

    object Main : NavigationRoute("Main")

    object Notes : NavigationRoute("Notes")

    object Details : NavigationRoute("Details")

    object Label : NavigationRoute("Label")

    object Settings : NavigationRoute("Settings")

    object HelpAndFeedback : NavigationRoute("HelpAndFeedback")
}

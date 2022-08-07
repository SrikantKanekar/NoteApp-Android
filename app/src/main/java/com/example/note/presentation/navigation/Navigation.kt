package com.example.note.presentation.navigation

sealed class Navigation(
    val route: String
) {
    object Splash : Navigation("Splash")

    object Main : Navigation("Main")

    object Notes : Navigation("Notes")

    object Details : Navigation("Details")

    object Label : Navigation("Label")

    object Settings : Navigation("Settings")

    object HelpAndFeedback : Navigation("HelpAndFeedback")
}

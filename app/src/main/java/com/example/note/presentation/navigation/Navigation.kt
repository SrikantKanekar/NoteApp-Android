package com.example.note.presentation.navigation

sealed class Navigation(
    val route: String
) {
    object Splash : Navigation("Splash")

    object Main : Navigation("Main")

    object NoteList : Navigation("NoteList")

    object NoteDetail : Navigation("NoteDetail")

    object Settings : Navigation("Settings")
}

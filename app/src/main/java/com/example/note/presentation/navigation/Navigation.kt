package com.example.note.presentation.navigation

sealed class Navigation(
    val route: String
) {
    object Splash : Navigation("Splash")

    object NoteList : Navigation("NoteList")

    object NoteDetail : Navigation("NoteDetail")

    object Settings : Navigation("Settings")
}

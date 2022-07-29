package com.example.note.presentation.navigation

sealed class Navigation(
    val route: String
) {
    object Splash : Navigation("Splash")

    object Main : Navigation("Main")

    object NoteList : Navigation("NoteList")

    object NoteDetail : Navigation("NoteDetail")

    object Search : Navigation("Search")

    object Reminders : Navigation("Reminders")

    object Archive : Navigation("Archive")

    object Deleted : Navigation("Deleted")

    object Settings : Navigation("Settings")

    object HelpAndFeedback : Navigation("HelpAndFeedback")

    object Label : Navigation("Label")

    object EditLabel : Navigation("EditLabel")
}

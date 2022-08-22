package com.example.note.model.enums

sealed class LabelScreenMode {
    object CREATE : LabelScreenMode()
    object EDIT : LabelScreenMode()
    class SELECT(val noteIds: List<String>) : LabelScreenMode()
}
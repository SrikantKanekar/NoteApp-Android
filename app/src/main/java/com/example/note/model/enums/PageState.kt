package com.example.note.model.enums

sealed class PageState {
    object NOTE: PageState()
    object REMINDER: PageState()
    class LABEL(val name: String): PageState()
    object ARCHIVE: PageState()
    object DELETED: PageState()
}
package com.example.note.model.enums

sealed class PageState {
    object NOTE: PageState()
    object REMINDER: PageState()
    class LABEL(val id: String): PageState()
    object ARCHIVE: PageState()
    object DELETED: PageState()
}
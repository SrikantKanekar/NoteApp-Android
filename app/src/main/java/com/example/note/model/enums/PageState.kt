package com.example.note.model.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PageState : Parcelable {
    object NOTE : PageState()
    object REMINDER : PageState()
    class LABEL(val id: String) : PageState()
    object ARCHIVE : PageState()
    object DELETED : PageState()
}
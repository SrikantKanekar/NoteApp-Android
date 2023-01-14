package com.example.note.model.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PageState : Parcelable {
    @Parcelize
    object NOTE : PageState()

    @Parcelize
    object REMINDER : PageState()

    @Parcelize
    class LABEL(val id: String) : PageState()

    @Parcelize
    object ARCHIVE : PageState()

    @Parcelize
    object DELETED : PageState()
}
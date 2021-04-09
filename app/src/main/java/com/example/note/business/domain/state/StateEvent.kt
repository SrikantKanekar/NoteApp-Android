package com.example.note.business.domain.state

interface StateEvent {
    fun errorInfo(): String

    fun eventName(): String

    fun shouldDisplayProgressBar(): Boolean
}
package com.example.note.framework.presentation.ui.noteDetail.state

import com.example.note.business.domain.state.StateEvent
import com.example.note.business.domain.state.StateMessage

sealed class NoteDetailStateEvent : StateEvent {

    class GetNoteEvent(val id: String) : NoteDetailStateEvent() {
        override fun errorInfo() = "Error getting note."
        override fun eventName() = "GetNoteEvent"
        override fun shouldDisplayProgressBar() = true
    }

    object UpdateNoteEvent : NoteDetailStateEvent() {
        override fun errorInfo() = "Error updating note."
        override fun eventName() = "UpdateNoteEvent"
        override fun shouldDisplayProgressBar() = true
    }

    class DeleteNoteEvent(
        val id: String
    ) : NoteDetailStateEvent() {
        override fun errorInfo() = "Error deleting note."
        override fun eventName() = "DeleteNoteEvent"
        override fun shouldDisplayProgressBar() = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ) : NoteDetailStateEvent() {
        override fun errorInfo() = "Error creating a new state message."
        override fun eventName() = "CreateStateMessageEvent"
        override fun shouldDisplayProgressBar() = false
    }
}
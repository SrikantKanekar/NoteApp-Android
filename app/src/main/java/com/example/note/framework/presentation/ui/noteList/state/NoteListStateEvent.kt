package com.example.note.framework.presentation.ui.noteList.state

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.StateEvent
import com.example.note.business.domain.state.StateMessage

sealed class NoteListStateEvent: StateEvent {

    class InsertNewNoteEvent(
        val title: String
    ): NoteListStateEvent() {
        override fun errorInfo() = "Error inserting new note."
        override fun eventName() = "InsertNewNoteEvent"
        override fun shouldDisplayProgressBar() = true
    }

    // for testing
    class InsertMultipleNotesEvent(
        val numNotes: Int
    ): NoteListStateEvent() {
        override fun errorInfo() = "Error inserting the notes."
        override fun eventName() = "InsertMultipleNotesEvent"
        override fun shouldDisplayProgressBar() = true
    }

    class DeleteNoteEvent(
        val note: Note
    ): NoteListStateEvent(){
        override fun errorInfo() = "Error deleting note."
        override fun eventName() = "DeleteNoteEvent"
        override fun shouldDisplayProgressBar() = true
    }

    class DeleteMultipleNotesEvent(
        val notes: List<Note>
    ): NoteListStateEvent(){
        override fun errorInfo() = "Error deleting the selected notes."
        override fun eventName() = "DeleteMultipleNotesEvent"
        override fun shouldDisplayProgressBar() = true
    }

    class RestoreDeletedNoteEvent(
        val note: Note
    ): NoteListStateEvent() {
        override fun errorInfo() = "Error restoring the note that was deleted."
        override fun eventName() = "RestoreDeletedNoteEvent"
        override fun shouldDisplayProgressBar() = false
    }

    class SearchNotesEvent(
        val clearLayoutManagerState: Boolean = true
    ): NoteListStateEvent(){
        override fun errorInfo() = "Error getting list of notes."
        override fun eventName() = "SearchNotesEvent"
        override fun shouldDisplayProgressBar() = true
    }

    object GetNumNotesInCacheEvent : NoteListStateEvent() {
        override fun errorInfo() = "Error getting the number of notes from the cache."
        override fun eventName() = "GetNumNotesInCacheEvent"
        override fun shouldDisplayProgressBar() = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): NoteListStateEvent(){
        override fun errorInfo() = "Error creating a new state message."
        override fun eventName() = "CreateStateMessageEvent"
        override fun shouldDisplayProgressBar() = false
    }
}
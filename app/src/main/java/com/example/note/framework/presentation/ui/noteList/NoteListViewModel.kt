package com.example.note.framework.presentation.ui.noteList

import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.state.StateEvent
import com.example.note.business.domain.util.printLogD
import com.example.note.business.interactors.noteList.NoteListInteractors
import com.example.note.framework.presentation.ui.BaseViewModel
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.*
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel
@Inject
constructor(
    private val noteInteractors: NoteListInteractors
) : BaseViewModel<NoteListViewState>() {

    override fun initViewState() = NoteListViewState()

    override fun handleNewData(data: NoteListViewState) {
        data.let { viewState ->
            viewState.noteList?.let { noteList ->
                printLogD("NoteListViewModel", "count ${noteList.size}")
            }

            viewState.numNotesInCache?.let { numNotes ->
                printLogD("NoteListViewModel", "count $numNotes")
            }

//            viewState.newNote?.let { note ->
//                setNote(note)
//            }

//            viewState.notePendingDelete?.let { restoredNote ->
//                restoredNote.note?.let { note ->
//                    setRestoredNoteId(note)
//                }
//                setNotePendingDelete(null)
//            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<NoteListViewState>?> = when (stateEvent) {

            is InsertNewNoteEvent -> {
                noteInteractors.insertNewNote.insertNewNote(
                    title = stateEvent.title,
                    stateEvent = stateEvent
                )
            }

            is DeleteNoteEvent -> {
                noteInteractors.deleteNote.deleteNote(
                    note = stateEvent.note,
                    stateEvent = stateEvent
                )
            }

            is GetNumNotesInCacheEvent -> {
                noteInteractors.getNumNotes.getNumNotes(
                    stateEvent = stateEvent
                )
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }
        launchJob(stateEvent, job)
    }
}
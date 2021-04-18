package com.example.note.framework.presentation.ui.noteDetail

import androidx.lifecycle.SavedStateHandle
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.state.StateEvent
import com.example.note.business.interactors.notedetail.NoteDetailInteractors
import com.example.note.framework.presentation.ui.BaseViewModel
import com.example.note.framework.presentation.ui.noteDetail.state.NoteDetailStateEvent.*
import com.example.note.framework.presentation.ui.noteDetail.state.NoteDetailViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel
@Inject
constructor(
    private val noteInteractors: NoteDetailInteractors,
    private val state: SavedStateHandle
) : BaseViewModel<NoteDetailViewState>() {

    init {
        state.get<NoteDetailViewState>("NoteDetailViewState")?.let {
            updateViewState(it)
        }
    }

    override fun initViewState(): NoteDetailViewState {
        return NoteDetailViewState()
    }

    override fun updateViewState(viewState: NoteDetailViewState) {
        setViewState(viewState)
        state.set<NoteDetailViewState>("NoteDetailViewState", viewState)
    }

    override fun handleNewData(data: NoteDetailViewState) {
        data.note?.let { note ->
            setViewState(viewState.value.copy(note = note, title = note.title, body = note.body))
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<NoteDetailViewState>?> = when (stateEvent) {

            is GetNoteEvent -> {
                noteInteractors.getNote.execute(
                    id = stateEvent.id,
                    stateEvent = stateEvent
                )
            }

            is UpdateNoteEvent -> {
                noteInteractors.updateNote.execute(
                    note = Note(
                        id = viewState.value.note!!.id,
                        title = viewState.value.title!!,
                        body = viewState.value.body!!,
                        updated_at = viewState.value.note!!.updated_at,
                        created_at = viewState.value.note!!.created_at
                    ),
                    stateEvent = stateEvent
                )
            }

            is DeleteNoteEvent -> {
                noteInteractors.deleteNote.deleteNote(
                    id = stateEvent.id,
                    stateEvent = stateEvent
                )
            }

            is CreateStateMessageEvent -> {
                emitStateMessageEvent(
                    stateMessage = stateEvent.stateMessage,
                    stateEvent = stateEvent
                )
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }
        launchJob(stateEvent, job)
    }

    fun updateNoteTitle(title: String) {
        setViewState(viewState.value.copy(title = title))
    }

    fun updateNoteBody(body: String) {
        setViewState(viewState.value.copy(body = body))
    }
}


package com.example.note.framework.presentation.ui.noteList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.state.*
import com.example.note.business.domain.util.printLogD
import com.example.note.business.interactors.notelist.NoteListInteractors
import com.example.note.framework.presentation.ui.BaseViewModel
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.*
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NoteListViewModel
@Inject
constructor(
    private val noteInteractors: NoteListInteractors,
    private val noteFactory: NoteFactory,
    private val state: SavedStateHandle
) : BaseViewModel<NoteListViewState>() {

    @ExperimentalCoroutinesApi
    val noteListFlow = viewState.flatMapLatest {
        noteInteractors.searchNotes.execute(it.searchQuery)
    }

    init {
        state.get<NoteListViewState>("NoteListViewState")?.let {
            updateViewState(it)
        }
        viewModelScope.launch {
            noteListFlow.collect {
                printLogD("", "note count ${it.size}")
                it.forEach {
                    printLogD("", it.id)
                }
            }
        }
    }

    // look for deleted note arg from detail screen, if so...then
    // 1.viewModel.setNotePendingDelete(note)
    // 2.showUndoSnackbar_deleteNote()
    // 3.clearArgs()

    // undo snackbar
    // undo --> viewModel.undoDelete()
    // else --> viewModel.setNotePendingDelete(null)
    // clear stateMessage

    // when MultiSelectionState --> viewModel.addOrRemoveNoteFromSelectedList(item)
    // else -> viewModel.setNote(item) or navigate

    // on swipe
    //if (!viewModel.isDeletePending()) {
    //    listAdapter.getNote(position).let { note ->
    //        viewModel.beginPendingDelete(note)
    //    }
    //} else {
    //    listAdapter.notifyDataSetChanged()
    //}

    ////call this method during swipeToRefresh, search, and after changing filter
    // viewModel.clearNoteList()
    // viewModel.loadFirstPage()

    override fun initViewState() = NoteListViewState()

    override fun updateViewState(viewState: NoteListViewState){
        setViewState(viewState)
        state.set<NoteListViewState>("NoteListViewState", viewState)
    }

    override fun handleNewData(data: NoteListViewState) {
        data.let { state ->
            
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<NoteListViewState>?> = when (stateEvent) {

            is InsertNewNoteEvent -> {
                noteInteractors.insertNewNote.execute(
                    note = stateEvent.note,
                    stateEvent = stateEvent
                )
            }

            is InsertMultipleNotesEvent -> {
                noteInteractors.insertMultipleNotes.execute(
                    numNotes = stateEvent.numNotes,
                    stateEvent = stateEvent
                )
            }

            is DeleteNoteEvent -> {
                printLogD("deleting note id", stateEvent.id)
                noteInteractors.deleteNote.execute(
                    id = stateEvent.id,
                    stateEvent = stateEvent
                )
            }

            is DeleteMultipleNotesEvent -> {
                noteInteractors.deleteMultipleNotes.execute(
                    notes = stateEvent.notes,
                    stateEvent = stateEvent
                )
            }

            is RestoreDeletedNoteEvent -> {
                noteInteractors.restoreDeletedNote.execute(
                    note = stateEvent.note,
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

    fun deleteNotes() {
        if (viewState.value.selectedNotes != null) {
            setStateEvent(DeleteMultipleNotesEvent(
                viewState.value.selectedNotes ?: ArrayList()
            ))
        } else {
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = "You haven't selected any notes to delete",
                            uiType = UiType.SnackBar,
                            messageType = MessageType.Info
                        )
                    )
                )
            )
        }
    }

    fun setSearchQuery(query: String) {
        updateViewState(viewState.value.copy(searchQuery = query))
    }

    fun createNewNote(): Note{
        return noteFactory.createSingleNote(
            title = Random.nextInt(from = 100, until = 1000).toString(),
            body = Random.nextInt(from = 100, until = 1000).toString()
        )
    }

    enum class NoteListToolbarState {
        SearchViewState, MultiSelectionState
    }
}

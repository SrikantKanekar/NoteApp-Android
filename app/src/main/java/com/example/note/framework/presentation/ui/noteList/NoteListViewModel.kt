package com.example.note.framework.presentation.ui.noteList

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.*
import com.example.note.business.interactors.notelist.DeleteMultipleNotes.Companion.DELETE_NOTES_YOU_MUST_SELECT
import com.example.note.business.interactors.notelist.NoteListInteractors
import com.example.note.framework.presentation.ui.BaseViewModel
import com.example.note.framework.presentation.ui.noteList.state.NoteListStateEvent.*
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState.NotePendingDelete
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val DELETE_PENDING_ERROR = "There is already a pending delete operation."

@HiltViewModel
class NoteListViewModel
@Inject
constructor(
    private val noteInteractors: NoteListInteractors,
) : BaseViewModel<NoteListViewState>() {

    init {
        setStateEvent(GetNumNotesInCacheEvent)
        setStateEvent(SearchNotesEvent)
    }

    // Store viewState for process death, remove noteList before storing

    // look for deleted note arg from detail screen, if so...then
    // 1.viewModel.setNotePendingDelete(note)
    // 2.showUndoSnackbar_deleteNote()
    // 3.clearArgs()

    //if (viewModel.isPaginationExhausted() && !viewModel.isQueryExhausted()) {
    //     viewModel.setQueryExhausted(true)
    //}

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

    // delete note, show dialog
    //  proceed() viewModel.deleteNotes()


    override fun initViewState() = NoteListViewState()

    override fun handleNewData(data: NoteListViewState) {
        data.let { state ->
            state.noteList?.let { noteList ->
                setViewState(viewState.value.copy(noteList = noteList))
            }

            state.numNotesInCache?.let { numNotes ->
                setViewState(viewState.value.copy(numNotesInCache = numNotes))
            }

            state.newNote?.let { note ->
                setViewState(viewState.value.copy(newNote = note))
            }

            state.notePendingDelete?.let { restoredNote ->
                restoredNote.note?.let { note ->
                    setRestoredNoteId(note)
                }
                setNotePendingDelete(null)
            }
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

            is InsertMultipleNotesEvent -> {
                noteInteractors.insertMultipleNotes.insertNotes(
                    numNotes = stateEvent.numNotes,
                    stateEvent = stateEvent
                )
            }

            is DeleteNoteEvent -> {
                noteInteractors.deleteNote.deleteNote(
                    note = stateEvent.note,
                    stateEvent = stateEvent
                )
            }

            is DeleteMultipleNotesEvent -> {
                noteInteractors.deleteMultipleNotes.deleteNotes(
                    notes = stateEvent.notes,
                    stateEvent = stateEvent
                )
            }

            is RestoreDeletedNoteEvent -> {
                noteInteractors.restoreDeletedNote.restoreDeletedNote(
                    note = stateEvent.note,
                    stateEvent = stateEvent
                )
            }

            is SearchNotesEvent -> {
                noteInteractors.searchNotes.searchNotes(
                    query = viewState.value.searchQuery ?: "",
                    stateEvent = stateEvent
                )
            }

            is GetNumNotesInCacheEvent -> {
                noteInteractors.getNumNotes.getNumNotes(
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

    private fun removeSelectedNotesFromList() {
        val list = viewState.value.noteList
        list?.removeAll(getSelectedNotes())
        setViewState(viewState.value.copy(noteList = list))
    }

    fun deleteNotes() {
        if (getSelectedNotes().size > 0) {
            setStateEvent(DeleteMultipleNotesEvent(getSelectedNotes()))
            removeSelectedNotesFromList()
        } else {
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = DELETE_NOTES_YOU_MUST_SELECT,
                            uiType = UiType.SnackBar,
                            messageType = MessageType.Info
                        )
                    )
                )
            )
        }
    }

    fun getSelectedNotes(): java.util.ArrayList<Note> {
        return viewState.value.selectedNotes ?: ArrayList()
    }

    fun setSearchQuery(query: String) {
        setViewState(viewState.value.copy(searchQuery = query))
    }


    // if a note is deleted and then restored, the id will be incorrect.
    // So need to reset it here.
    private fun setRestoredNoteId(restoredNote: Note) {
        var noteList = viewState.value.noteList
        noteList?.let { list ->
            for ((index, note) in list.withIndex()) {
                if (note.title == restoredNote.title) {
                    list.remove(note)
                    list.add(index, restoredNote)
                    noteList = list
                    break
                }
            }
        }
        setViewState(viewState.value.copy(noteList = noteList))
    }

    fun isDeletePending(): Boolean {
        val pendingNote = viewState.value.notePendingDelete
        if (pendingNote != null) {
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = DELETE_PENDING_ERROR,
                            uiType = UiType.SnackBar,
                            messageType = MessageType.Info
                        )
                    )
                )
            )
            return true
        } else {
            return false
        }
    }

    fun beginPendingDelete(note: Note) {
        setNotePendingDelete(note)
        removePendingNoteFromList(note)
        setStateEvent(
            DeleteNoteEvent(
                note = note
            )
        )
    }

    private fun removePendingNoteFromList(note: Note?) {
        val noteList = viewState.value.noteList
        if (noteList?.contains(note) == true) {
            noteList.remove(note)
            setViewState(viewState.value.copy(noteList = noteList))
        }
    }

    fun undoDelete() {
        // replace note in viewState
        val viewState = viewState.value
        viewState.notePendingDelete?.let { note ->
            if (note.listPosition != null && note.note != null) {
                viewState.noteList?.add(
                    note.listPosition as Int,
                    note.note as Note
                )
                setStateEvent(RestoreDeletedNoteEvent(note.note as Note))
            }
        }
        setViewState(viewState)
    }


    private fun setNotePendingDelete(note: Note?) {
        val notePendingDelete = note?.let {
            NotePendingDelete(
                note = it,
                listPosition = viewState.value.noteList?.indexOf(it)
            )
        }
        setViewState(viewState.value.copy(notePendingDelete = notePendingDelete))
    }

    fun isPaginationExhausted(): Boolean {
        val count = viewState.value.noteList?.size ?: 0
        val cacheCount = viewState.value.numNotesInCache ?: 0
        return count >= cacheCount
    }

    fun addOrRemoveNoteFromSelectedList(note: Note) {
        var list = viewState.value.selectedNotes
        if (list == null) {
            list = ArrayList()
        }
        if (list.contains(note)) {
            list.remove(note)
        } else {
            list.add(note)
        }
        setViewState(viewState.value.copy(selectedNotes = list))
    }

    enum class NoteListToolbarState {
        SearchViewState, MultiSelectionState
    }
}

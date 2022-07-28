package com.example.note.presentation.ui.noteList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.model.Note
import com.example.note.repository.NoteRepository
import com.example.note.util.NOTE_LIST_STATE
import com.example.note.util.NoteFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val noteFactory: NoteFactory,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteListUiState())
    val uiState: StateFlow<NoteListUiState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    val noteListFlow = uiState.flatMapLatest {
        noteRepository.searchNotes(it.searchQuery)
    }

    init {
        state.get<NoteListUiState>(NOTE_LIST_STATE)?.let { state ->
            _uiState.value = state
        }
    }

    // look for deleted note arg from detail screen, if so...then
    // 1.viewModel.setNotePendingDelete(note)
    // 2.showUndoSnackBar_deleteNote()
    // 3.clearArgs()

    // undo snackBar
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

    fun insertNewNote(note: Note) {
        viewModelScope.launch {
            try {
                noteRepository.insertNote(note)
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(errorMessage = e.message)
            }
            state.set<NoteListUiState>(NOTE_LIST_STATE, uiState.value)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                noteRepository.deleteNote(note)
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(errorMessage = e.message)
            }
            state.set<NoteListUiState>(NOTE_LIST_STATE, uiState.value)
        }
    }

    fun deleteMultipleNotes() {
        viewModelScope.launch {
            try {
                noteRepository.deleteNotes(uiState.value.selectedNotes)
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(errorMessage = e.message)
            }
            state.set<NoteListUiState>(NOTE_LIST_STATE, uiState.value)
        }
    }

    fun restoreDeletedNote(note: Note) {
        viewModelScope.launch {
            try {
                noteRepository.restoreDeletedNote(note)
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(errorMessage = e.message)
            }
            state.set<NoteListUiState>(NOTE_LIST_STATE, uiState.value)
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.value = uiState.value.copy(searchQuery = query)
    }

    fun errorMessageShown() {
        _uiState.value = uiState.value.copy(errorMessage = null)
    }

    fun createNewNote(): Note {
        return noteFactory.createSingleNote(
            title = Random.nextInt(from = 100, until = 1000).toString(),
            body = Random.nextInt(from = 100, until = 1000).toString()
        )
    }

    enum class NoteListToolbarState {
        SearchViewState, MultiSelectionState
    }
}

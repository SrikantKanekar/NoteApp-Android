package com.example.note.presentation.ui.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.model.Note
import com.example.note.repository.NoteRepository
import com.example.note.util.NOTES_STATE
import com.example.note.util.NoteFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val noteFactory: NoteFactory,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    val notesFlow = uiState.flatMapLatest {
        noteRepository.searchNotes()
    }

    init {
        state.get<NotesUiState>(NOTES_STATE)?.let { state ->
            _uiState.value = state
        }
    }

    fun insertNewNote(note: Note) {
        viewModelScope.launch {
            try {
                noteRepository.insertNote(note)
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(errorMessage = e.message)
            }
            state.set<NotesUiState>(NOTES_STATE, uiState.value)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                noteRepository.deleteNote(note)
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(errorMessage = e.message)
            }
            state.set<NotesUiState>(NOTES_STATE, uiState.value)
        }
    }

    fun deleteMultipleNotes() {
        viewModelScope.launch {
            try {
                noteRepository.deleteNotes(uiState.value.selectedNotes)
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(errorMessage = e.message)
            }
            state.set<NotesUiState>(NOTES_STATE, uiState.value)
        }
    }

    fun restoreDeletedNote(note: Note) {
        viewModelScope.launch {
            try {
                noteRepository.restoreDeletedNote(note)
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(errorMessage = e.message)
            }
            state.set<NotesUiState>(NOTES_STATE, uiState.value)
        }
    }

    fun errorMessageShown() {
        _uiState.value = uiState.value.copy(errorMessage = null)
    }

    fun createNewNote(): Note {
        return noteFactory.createNote()
    }
}

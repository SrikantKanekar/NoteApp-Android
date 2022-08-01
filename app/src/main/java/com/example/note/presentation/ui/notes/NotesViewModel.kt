package com.example.note.presentation.ui.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.model.Note
import com.example.note.model.enums.CardLayoutType
import com.example.note.model.enums.PageState.*
import com.example.note.repository.NoteRepository
import com.example.note.util.NOTES_STATE
import com.example.note.util.NoteFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    init {
        state.get<NotesUiState>(NOTES_STATE)?.let { state ->
            _uiState.update { state }
        }
        viewModelScope.launch {
            noteRepository.searchNotes().collect { notes ->
                when (uiState.value.pageState) {
                    ARCHIVE -> {}
                    DELETED -> {}
                    is LABEL -> {}
                    NOTE -> {}
                    REMINDER -> {}
                    SEARCH -> {}
                }
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            try {
                noteRepository.insertNote(note)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
            state.set<NotesUiState>(NOTES_STATE, _uiState.value)
        }
    }

    fun updateCardLayoutType(cardLayoutType: CardLayoutType) {
        _uiState.update { it.copy(cardLayoutType = cardLayoutType) }
    }

    fun updateSelectedNotes(note: Note) {
        val selectedNotes = ArrayList(_uiState.value.selectedNotes)
        when (selectedNotes.contains(note)) {
            false -> selectedNotes.add(note)
            true -> selectedNotes.remove(note)
        }
        _uiState.update { it.copy(selectedNotes = selectedNotes) }
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            try {
                noteRepository.deleteNotes(_uiState.value.selectedNotes)
                _uiState.update {
                    it.copy(selectedNotes = listOf(), errorMessage = "Notes moved to bin")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(selectedNotes = listOf(), errorMessage = e.message) }
            }
            state.set<NotesUiState>(NOTES_STATE, _uiState.value)
        }
    }

    fun clearSelectedNotes() = _uiState.update { it.copy(selectedNotes = listOf()) }

    fun errorMessageShown() = _uiState.update { it.copy(errorMessage = null) }

    fun createNewNote(): Note = noteFactory.createNote()
}

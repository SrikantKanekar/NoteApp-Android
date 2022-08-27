package com.example.note.presentation.ui.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.model.Note
import com.example.note.model.NoteGrid
import com.example.note.model.enums.CardLayoutType
import com.example.note.model.enums.NoteState
import com.example.note.model.enums.PageState
import com.example.note.model.enums.PageState.*
import com.example.note.repository.LabelRepository
import com.example.note.repository.NoteRepository
import com.example.note.util.NOTES_STATE
import com.example.note.util.NoteFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val labelRepository: LabelRepository,
    private val noteFactory: NoteFactory,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState

    private var job: Job? = null

    init {
        state.get<NotesUiState>(NOTES_STATE)?.let { _uiState.update { it } }
        refreshNotes()
        fetchLabels()
    }

    private fun refreshNotes() {
        job?.cancel()
        job = viewModelScope.launch {
            noteRepository.searchNotes(uiState.value.query).collect { notes ->
                val noteGrids = arrayListOf<NoteGrid>()
                when (val pageState = uiState.value.pageState) {
                    NOTE -> {
                        val activeNotes = notes.filter { it.state == NoteState.ACTIVE }
                        val pinned = activeNotes.any { it.pinned }
                        if (pinned) {
                            val pinnedGrid = NoteGrid(activeNotes.filter { it.pinned }, "Pinned")
                            val othersGrid = NoteGrid(activeNotes.filter { !it.pinned }, "Others")
                            noteGrids.add(pinnedGrid)
                            noteGrids.add(othersGrid)
                        } else {
                            val grid = NoteGrid(activeNotes)
                            noteGrids.add(grid)
                        }
                    }
                    REMINDER -> { }
                    is LABEL -> {
                        val groups = notes
                            .filter { it.labels.contains(pageState.id) }
                            .groupBy { it.state }
                        val activeNotes = groups[NoteState.ACTIVE] ?: listOf()
                        val archiveNotes = groups[NoteState.ARCHIVED] ?: listOf()

                        noteGrids.add(NoteGrid(activeNotes))
                        noteGrids.add(NoteGrid(archiveNotes, "Archive"))
                    }
                    ARCHIVE -> {
                        val grid = NoteGrid(notes.filter { it.state == NoteState.ARCHIVED })
                        noteGrids.add(grid)
                    }
                    DELETED -> {
                        val grid = NoteGrid(notes.filter { it.state == NoteState.DELETED })
                        noteGrids.add(grid)
                    }
                }
                _uiState.update { it.copy(noteGrids = noteGrids) }
                state.set<NotesUiState>(NOTES_STATE, _uiState.value)
            }
        }
    }

    private fun fetchLabels() {
        viewModelScope.launch {
            labelRepository.searchLabels().collect { labels ->
                _uiState.update { it.copy(labels = labels) }
                state.set<NotesUiState>(NOTES_STATE, _uiState.value)
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

    fun updatePageState(pageState: PageState) {
        _uiState.update { it.copy(pageState = pageState) }
        refreshNotes()
    }

    fun updateCardLayoutType(cardLayoutType: CardLayoutType) {
        _uiState.update { it.copy(cardLayoutType = cardLayoutType) }
        state.set<NotesUiState>(NOTES_STATE, _uiState.value)
    }

    fun updateSelectedNotes(note: Note) {
        val selectedNotes = ArrayList(_uiState.value.selectedNotes)
        when (selectedNotes.contains(note)) {
            false -> selectedNotes.add(note)
            true -> selectedNotes.remove(note)
        }
        _uiState.update { it.copy(selectedNotes = selectedNotes) }
        state.set<NotesUiState>(NOTES_STATE, _uiState.value)
    }

    fun pinOrUnpinSelectedNotes() {
        viewModelScope.launch {
            try {
                val selectedNotes = _uiState.value.selectedNotes
                val pin = selectedNotes.any { !it.pinned }
                val updatedNotes = selectedNotes.map { it.copy(pinned = pin) }
                noteRepository.updateNotes(updatedNotes)
                _uiState.update { it.copy(selectedNotes = listOf()) }
            } catch (e: Exception) {
                _uiState.update { it.copy(selectedNotes = listOf(), errorMessage = e.message) }
            }
            state.set<NotesUiState>(NOTES_STATE, _uiState.value)
        }
    }

    fun archiveOrUnarchiveSelectedNotes() {
        viewModelScope.launch {
            try {
                val selectedNotes = _uiState.value.selectedNotes
                val archive = selectedNotes.any { it.state != NoteState.ARCHIVED }
                val updatedNotes = selectedNotes.map {
                    it.copy(state = if (archive) NoteState.ARCHIVED else NoteState.ACTIVE)
                }
                noteRepository.updateNotes(updatedNotes)
                _uiState.update {
                    it.copy(
                        selectedNotes = listOf(),
                        errorMessage = if (updatedNotes.size > 1) "Notes archived" else "Note archived"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(selectedNotes = listOf(), errorMessage = e.message) }
            }
            state.set<NotesUiState>(NOTES_STATE, _uiState.value)
        }
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            try {
                val updatedNotes = _uiState.value.selectedNotes.map {
                    it.copy(state = NoteState.DELETED)
                }
                noteRepository.updateNotes(updatedNotes)
                _uiState.update {
                    it.copy(selectedNotes = listOf(), errorMessage = "Notes moved to bin")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(selectedNotes = listOf(), errorMessage = e.message) }
            }
            state.set<NotesUiState>(NOTES_STATE, _uiState.value)
        }
    }

    fun clearSelectedNotes() {
        _uiState.update { it.copy(selectedNotes = listOf()) }
        state.set<NotesUiState>(NOTES_STATE, _uiState.value)
    }

    fun setSearchMode() {
        _uiState.update { it.copy(isSearch = true) }
        state.set<NotesUiState>(NOTES_STATE, _uiState.value)
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        refreshNotes()
    }

    fun onSearchBackClick() {
        _uiState.update { it.copy(isSearch = false) }
        onQueryChange("")
    }

    fun onSearchClearClick() {
        onQueryChange("")
    }

    fun errorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
        state.set<NotesUiState>(NOTES_STATE, _uiState.value)
    }

    fun createNewNote(): Note = noteFactory.createNote()
}

package com.example.note.presentation.ui.labels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.model.Label
import com.example.note.repository.LabelRepository
import com.example.note.repository.NoteRepository
import com.example.note.util.LABELS_STATE
import com.example.note.util.LabelFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelsViewModel @Inject constructor(
    private val labelRepository: LabelRepository,
    private val noteRepository: NoteRepository,
    private val labelFactory: LabelFactory,
    private val state: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(LabelsUiState())
        private set

    private var job: Job? = null

    init {
        state.get<LabelsUiState>(LABELS_STATE)?.let { uiState = it }
        refreshLabels()
    }

    private fun refreshLabels() {
        job?.cancel()
        job = viewModelScope.launch {
            labelRepository.searchLabels(uiState.query).collect { labels ->
                uiState = uiState.copy(labels = labels)
                state.set<LabelsUiState>(LABELS_STATE, uiState)
            }
        }
    }

    fun createLabel(name: String) {
        if (name.isNotEmpty()) {
            val label = labelFactory.createLabel(name = name)
            insertLabel(label)
        }
    }

    private fun insertLabel(label: Label) {
        viewModelScope.launch {
            try {
                labelRepository.insertLabel(label)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message)
            }
            state.set<LabelsUiState>(LABELS_STATE, uiState)
        }
    }

    fun updateLabel(label: Label) {
        viewModelScope.launch {
            try {
                labelRepository.updateLabel(label)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message)
            }
            state.set<LabelsUiState>(LABELS_STATE, uiState)
        }
    }

    fun deleteLabel(label: Label) {
        viewModelScope.launch {
            try {
                labelRepository.deleteLabel(label)
                val updatedNotes = noteRepository.getAllNotes().map { note ->
                    val noteLabels = ArrayList(note.labels)
                    noteLabels.remove(label.id)
                    note.copy(labels = noteLabels)
                }
                noteRepository.updateNotes(updatedNotes)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message)
            }
            state.set<LabelsUiState>(LABELS_STATE, uiState)
        }
    }

    fun fetchSelectedNotes(noteIds: List<String>) {
        viewModelScope.launch {
            try {
                noteRepository.searchNotes().collect { allNotes ->
                    val selectedNotes = allNotes.filter { noteIds.contains(it.id) }
                    uiState = uiState.copy(selectedNotes = selectedNotes)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message)
            }
            state.set<LabelsUiState>(LABELS_STATE, uiState)
        }
    }

    fun updateLabelsOfSelectedNotes(labelId: String, value: Boolean) {
        viewModelScope.launch {
            try {
                val updatedNotes = uiState.selectedNotes.map { note ->
                    val noteLabels = ArrayList(note.labels)
                    when (value) {
                        true -> if (!noteLabels.contains(labelId)) noteLabels.add(labelId)
                        false -> noteLabels.remove(labelId)
                    }
                    note.copy(labels = noteLabels)
                }
                noteRepository.updateNotes(updatedNotes)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message)
            }
            state.set<LabelsUiState>(LABELS_STATE, uiState)
        }
    }

    fun onQueryChange(query: String) {
        uiState = uiState.copy(query = query)
        refreshLabels()
    }

    fun errorMessageShown() {
        uiState = uiState.copy(errorMessage = null)
        state.set<LabelsUiState>(LABELS_STATE, uiState)
    }
}
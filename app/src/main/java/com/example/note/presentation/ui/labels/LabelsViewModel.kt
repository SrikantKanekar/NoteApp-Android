package com.example.note.presentation.ui.labels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.repository.LabelRepository
import com.example.note.util.LABELS_STATE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelsViewModel @Inject constructor(
    private val labelRepository: LabelRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(LabelsUiState())
        private set

    init {
        state.get<LabelsUiState>(LABELS_STATE)?.let { state ->
            uiState = state
        }
    }

    fun fetchLabels() {
        viewModelScope.launch {
            uiState = try {
                val labels = labelRepository.getAllLabels()
                uiState.copy(labels = labels)
            } catch (e: Exception) {
                uiState.copy(errorMessage = e.message)
            }
            state.set<LabelsUiState>(LABELS_STATE, uiState)
        }
    }

    fun errorMessageShown() {
        uiState = uiState.copy(errorMessage = null)
    }
}
package com.example.note.presentation.ui.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.repository.NoteRepository
import com.example.note.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    var synced by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                noteRepository.syncNotes()
            } catch (e: Exception) {
                printLogD("SplashViewModel", e.message.toString())
            } finally {
                synced = true
            }
        }
    }
}
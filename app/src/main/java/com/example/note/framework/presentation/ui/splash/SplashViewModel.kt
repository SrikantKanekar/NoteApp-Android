package com.example.note.framework.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.business.interactors.splash.NoteSyncInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val noteSyncInteractors: NoteSyncInteractors
) : ViewModel() {

    private val _hasSyncBeenExecuted = MutableStateFlow(false)

    val hasSyncBeenExecuted: StateFlow<Boolean> = _hasSyncBeenExecuted

    init {
        executeDataSync(viewModelScope)
    }

    private fun executeDataSync(coroutineScope: CoroutineScope) {
        if (_hasSyncBeenExecuted.value) {
            return
        }

        val syncJob = coroutineScope.launch {
            launch {
                noteSyncInteractors.syncDeletedNotes.syncDeletedNotes()
            }.join()

            launch {
                noteSyncInteractors.syncNotes.syncNotes()
            }
        }

        syncJob.invokeOnCompletion {
            CoroutineScope(Main).launch {
                _hasSyncBeenExecuted.value = true
            }
        }
    }
}
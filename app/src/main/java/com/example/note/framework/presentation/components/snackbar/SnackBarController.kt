package com.example.note.framework.presentation.components.snackbar

import androidx.compose.material.ScaffoldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * If a snackBar is visible and the user triggers a second snackBar to show, it will remove
 * the first one and show the second. Likewise with a third, fourth, ect...
 *
 * If a mechanism like this is not used, snackBar get added to the Scaffolds "queue", and will
 * show one after another. I don't like that.
 */
class SnackBarController
constructor(
    private val scope: CoroutineScope
) {

    private var snackBarJob: Job? = null

    init {
        cancelActiveJob()
    }

    fun getScope() = scope

    fun showSnackBar(
        scaffoldState: ScaffoldState,
        message: String,
        actionLabel: String,
        removeStateMessage: () -> Unit
    ) {

        if (snackBarJob == null) {
            snackBarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel
                )
                removeStateMessage()
                cancelActiveJob()
            }
        } else {
            cancelActiveJob()
            snackBarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel
                )
                removeStateMessage()
                cancelActiveJob()
            }
        }
    }

    private fun cancelActiveJob() {
        snackBarJob?.let { job ->
            job.cancel()
            snackBarJob = Job()
        }
    }
}
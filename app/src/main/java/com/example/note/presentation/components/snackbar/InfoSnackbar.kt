package com.example.note.presentation.components.snackbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoSnackBar(
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit?
) {
    SnackbarHost(
        hostState = snackBarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = { Text(text = data.visuals.message) },
                action = {
                    data.visuals.actionLabel?.let { actionLabel ->
                        Text(
                            modifier = Modifier
                                .clickable { onDismiss() }
                                .padding(end = 8.dp),
                            text = actionLabel
                        )
                    }
                }
            )
        },
        modifier = modifier
    )
}
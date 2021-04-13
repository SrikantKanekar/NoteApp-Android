package com.example.note.framework.presentation.components.snackbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit?
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = { Text(text = data.message) },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        Text(
                            modifier = Modifier.padding(end = 8.dp).clickable { onDismiss() },
                            text = actionLabel
                        )
                    }
                }
            )
        },
        modifier = modifier
    )
}
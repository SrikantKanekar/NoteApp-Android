package com.example.note.framework.presentation.components.snackbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SuccessSnackBar(
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit?
) {
    SnackbarHost(
        hostState = snackBarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = { Text(text = data.message) },
                action = {
                    data.actionLabel?.let { actionLabel ->
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
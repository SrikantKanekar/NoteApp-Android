package com.example.note.presentation.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.note.presentation.components.CircularButton

@Composable
fun MyInfoDialog(
    modifier: Modifier = Modifier,
    message: String,
    removeStateMessage: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = "Info") },
        text = { Text(text = message) },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularButton(
                    text = "Ok",
                    onClick = { removeStateMessage() },
                )
            }
        },
        onDismissRequest = {
            removeStateMessage()
        }
    )
}

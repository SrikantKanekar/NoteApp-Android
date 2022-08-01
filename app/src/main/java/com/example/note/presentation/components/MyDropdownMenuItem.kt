package com.example.note.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun MyDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
) {

    DropdownMenuItem(
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        },
        onClick = onClick,
        contentPadding = PaddingValues(start = 25.dp, end = 60.dp)
    )
}
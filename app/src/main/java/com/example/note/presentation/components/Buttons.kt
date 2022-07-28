package com.example.note.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .height(45.dp)
            .clip(CircleShape),
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = text.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
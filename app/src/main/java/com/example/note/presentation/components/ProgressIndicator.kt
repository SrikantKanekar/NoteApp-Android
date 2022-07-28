package com.example.note.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyCircularProgressIndicator(
    isDisplayed: Boolean,
    modifier: Modifier = Modifier
) {
    if (isDisplayed) {
        CircularProgressIndicator(
            modifier = modifier
        )
    }
}
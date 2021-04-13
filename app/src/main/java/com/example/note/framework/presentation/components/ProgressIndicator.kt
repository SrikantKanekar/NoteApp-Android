package com.example.note.framework.presentation.components

import androidx.compose.material.CircularProgressIndicator
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
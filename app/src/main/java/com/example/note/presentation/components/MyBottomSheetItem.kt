package com.example.note.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomSheetItem(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit,
    contentDescription: String?
) {
    ListItem(
        modifier = Modifier.clickable {
            onClick()
        },
        leadingContent = {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription
            )
        },
        headlineText = {
            Text(text = text)
        }
    )
}
package com.example.note.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MyIconButton(
    isSelected: Boolean,
    filledIcon: ImageVector,
    outlinedIcon: ImageVector,
    filledDescription: String?,
    outlinedDescription: String?,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        if (isSelected) {
            Icon(
                imageVector = filledIcon,
                contentDescription = filledDescription,
            )
        } else {
            Icon(
                imageVector = outlinedIcon,
                contentDescription = outlinedDescription,
            )
        }
    }
}

@Composable
fun MyIconButton(
    icon: ImageVector,
    description: String?,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
        )
    }
}
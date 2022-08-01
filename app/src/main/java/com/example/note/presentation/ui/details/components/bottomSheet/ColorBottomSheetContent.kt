package com.example.note.presentation.ui.details.components.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.runtime.Composable
import com.example.note.presentation.components.MyBottomSheetItem

@Composable
fun ColorBottomSheetContent(
    onColorClick: () -> Unit,
    onBackgroundClick: () -> Unit,
) {
    Column {
        MyBottomSheetItem(
            imageVector = Icons.Outlined.Camera,
            text = "Colour",
            onClick = onColorClick,
            contentDescription = "Colour"
        )
        MyBottomSheetItem(
            imageVector = Icons.Outlined.Camera,
            text = "Background",
            onClick = onBackgroundClick,
            contentDescription = "Background"
        )
    }
}
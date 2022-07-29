package com.example.note.presentation.ui.noteDetail.components.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.runtime.Composable
import com.example.note.presentation.components.BottomSheetItem

@Composable
fun ColorBottomSheetContent(
    onColorClick: () -> Unit,
    onBackgroundClick: () -> Unit,
) {
    Column {
        BottomSheetItem(
            imageVector = Icons.Outlined.Camera,
            text = "Colour",
            onClick = onColorClick,
            contentDescription = "Colour"
        )
        BottomSheetItem(
            imageVector = Icons.Outlined.Camera,
            text = "Background",
            onClick = onBackgroundClick,
            contentDescription = "Background"
        )
    }
}
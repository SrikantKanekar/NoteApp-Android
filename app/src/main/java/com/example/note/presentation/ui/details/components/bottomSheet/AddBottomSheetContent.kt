package com.example.note.presentation.ui.details.components.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import com.example.note.presentation.components.BottomSheetItem

@Composable
fun AddBottomSheetContent(
    onTakePhotoClick: () -> Unit,
    onAddImageClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onRecordingClick: () -> Unit,
    onTickBoxesClick: () -> Unit,
) {
    Column {
        BottomSheetItem(
            imageVector = Icons.Outlined.CameraAlt,
            text = "Take photo",
            onClick = onTakePhotoClick,
            contentDescription = "Take photo"
        )
        BottomSheetItem(
            imageVector = Icons.Outlined.Image,
            text = "Add image",
            onClick = onAddImageClick,
            contentDescription = "Add image"
        )
        BottomSheetItem(
            imageVector = Icons.Outlined.Brush,
            text = "Drawing",
            onClick = onDrawingClick,
            contentDescription = "Drawing"
        )
        BottomSheetItem(
            imageVector = Icons.Outlined.MicNone,
            text = "Recording",
            onClick = onRecordingClick,
            contentDescription = "Recording"
        )
        BottomSheetItem(
            imageVector = Icons.Outlined.CheckBox,
            text = "Tick boxes",
            onClick = onTickBoxesClick,
            contentDescription = "Tick boxes"
        )
    }
}
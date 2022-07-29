package com.example.note.presentation.ui.noteDetail.components.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import com.example.note.presentation.components.BottomSheetItem

@Composable
fun MoreBottomSheetContent(
    onDeleteClick: () -> Unit,
    onMakeCopyClick: () -> Unit,
    onSendClick: () -> Unit,
    onCollaboratorClick: () -> Unit,
    onLabelsClick: () -> Unit,
) {
    Column {
        BottomSheetItem(
            imageVector = Icons.Outlined.Delete,
            text = "Delete",
            onClick = onDeleteClick,
            contentDescription = "Delete"
        )
        BottomSheetItem(
            imageVector = Icons.Outlined.ContentCopy,
            text = "Make a copy",
            onClick = onMakeCopyClick,
            contentDescription = "Make a copy"
        )
        BottomSheetItem(
            imageVector = Icons.Outlined.Share,
            text = "Send",
            onClick = onSendClick,
            contentDescription = "Send"
        )
        BottomSheetItem(
            imageVector = Icons.Outlined.PersonAdd,
            text = "Collaborator",
            onClick = onCollaboratorClick,
            contentDescription = "Collaborator"
        )
        BottomSheetItem(
            imageVector = Icons.Outlined.Label,
            text = "Labels",
            onClick = onLabelsClick,
            contentDescription = "Labels"
        )
    }
}
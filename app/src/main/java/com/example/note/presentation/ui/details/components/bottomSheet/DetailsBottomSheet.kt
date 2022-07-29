package com.example.note.presentation.ui.details.components.bottomSheet

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.note.presentation.ui.details.DetailsBottomSheetType
import com.example.note.presentation.ui.details.DetailsBottomSheetType.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsBottomSheet(
    sheetType: DetailsBottomSheetType,
    sheetState: ModalBottomSheetState,
    onTakePhotoClick: () -> Unit,
    onAddImageClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onRecordingClick: () -> Unit,
    onTickBoxesClick: () -> Unit,
    onColorClick: () -> Unit,
    onBackgroundClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMakeCopyClick: () -> Unit,
    onSendClick: () -> Unit,
    onCollaboratorClick: () -> Unit,
    onLabelsClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            when (sheetType) {
                ADD -> AddBottomSheetContent(
                    onTakePhotoClick = onTakePhotoClick,
                    onAddImageClick = onAddImageClick,
                    onDrawingClick = onDrawingClick,
                    onRecordingClick = onRecordingClick,
                    onTickBoxesClick = onTickBoxesClick
                )
                COLOR -> ColorBottomSheetContent(
                    onColorClick = onColorClick,
                    onBackgroundClick = onBackgroundClick
                )
                MORE -> MoreBottomSheetContent(
                    onDeleteClick = onDeleteClick,
                    onMakeCopyClick = onMakeCopyClick,
                    onSendClick = onSendClick,
                    onCollaboratorClick = onCollaboratorClick,
                    onLabelsClick = onLabelsClick
                )
            }
        },
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        content = content
    )
}
package com.example.note.presentation.ui.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.note.model.enums.DetailsBottomSheetType.*
import com.example.note.presentation.components.MyCircularProgressIndicator
import com.example.note.presentation.ui.details.components.DetailsBottomAppBar
import com.example.note.presentation.ui.details.components.DetailsTopAppBar
import com.example.note.presentation.ui.details.components.NoteBody
import com.example.note.presentation.ui.details.components.NoteTitle
import com.example.note.presentation.ui.details.components.bottomSheet.DetailsBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DetailsScreen(
    noteId: String,
    navigateBack: () -> Unit
) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val uiState = viewModel.uiState

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val sheetType = remember { mutableStateOf(ADD) }

    LaunchedEffect(Unit) {
        viewModel.getNote(noteId)
    }

    DetailsBottomSheet(
        sheetType = sheetType.value,
        sheetState = sheetState,
        onTakePhotoClick = { },
        onAddImageClick = { },
        onDrawingClick = { },
        onRecordingClick = { },
        onTickBoxesClick = { },
        onColorClick = { },
        onBackgroundClick = { },
        onDeleteClick = {
            navigateBack()
            viewModel.deleteNote()
        },
        onMakeCopyClick = { },
        onSendClick = { },
        onCollaboratorClick = { },
        onLabelsClick = { }
    ) {
        Scaffold(
            topBar = {
                DetailsTopAppBar(
                    onBackPressed = {
                        navigateBack()
                        viewModel.updateNote()
                    },
                    onPinClick = {},
                    onReminderClick = {},
                    onArchiveClick = {}
                )
            },
            snackbarHost = { SnackbarHost(snackBarHostState) },
            bottomBar = {
                DetailsBottomAppBar(
                    updatedAt = uiState.note?.updated_at,
                    onAddClick = {
                        sheetType.value = ADD
                        scope.launch { sheetState.show() }
                    },
                    onColorClick = {
                        sheetType.value = COLOR
                        scope.launch { sheetState.show() }
                    },
                    onMoreClick = {
                        sheetType.value = MORE
                        scope.launch { sheetState.show() }
                    },
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues)
                        .padding(horizontal = 22.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(25.dp)
                ) {

                    val titleRequester = remember { FocusRequester() }
                    val bodyRequester = remember { FocusRequester() }

                    LaunchedEffect(Unit) {
                        titleRequester.requestFocus()
                    }

                    NoteTitle(
                        modifier = Modifier.focusRequester(titleRequester),
                        value = uiState.title,
                        onValueChange = { title ->
                            viewModel.updateNoteTitle(title)
                        },
                        focusRequester = bodyRequester
                    )

                    NoteBody(
                        modifier = Modifier.focusRequester(bodyRequester),
                        value = uiState.body,
                        onValueChange = { body ->
                            viewModel.updateNoteBody(body)
                        }
                    )
                }
            }
        )
    }

    MyCircularProgressIndicator(isDisplayed = uiState.isLoading)

    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackBarHostState.showSnackbar(errorMessage)
            viewModel.errorMessageShown()
        }
    }

    BackHandler {
        when (sheetState.isVisible) {
            false -> {
                navigateBack()
                viewModel.updateNote()
            }
            true -> scope.launch { sheetState.hide() }
        }
    }
}
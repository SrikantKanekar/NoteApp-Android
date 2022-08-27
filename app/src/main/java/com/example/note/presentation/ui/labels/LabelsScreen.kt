package com.example.note.presentation.ui.labels

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.note.model.enums.LabelScreenMode
import com.example.note.model.enums.LabelScreenMode.CREATE
import com.example.note.model.enums.LabelScreenMode.SELECT
import com.example.note.presentation.components.MyCircularProgressIndicator
import com.example.note.presentation.ui.labels.components.LabelEditItem
import com.example.note.presentation.ui.labels.components.LabelSelectItem
import com.example.note.presentation.ui.labels.components.LabelsTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelsScreen(
    mode: LabelScreenMode,
    navigateBack: () -> Unit
) {

    val viewModel = hiltViewModel<LabelsViewModel>()
    val uiState = viewModel.uiState

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val createFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        when (mode) {
            is SELECT -> viewModel.fetchSelectedNotes(mode.noteIds)
            is CREATE -> createFocusRequester.requestFocus()
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LabelsTopAppBar(
                scrollBehavior = scrollBehavior,
                mode = mode,
                query = uiState.query,
                onQueryChange = { viewModel.onQueryChange(it) },
                navigateBack = navigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { paddingValues ->
            val labelNames = uiState.labels.map { it.name }
            val query = uiState.query

            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                content = {
                    item {
                        when (mode) {
                            is SELECT -> {
                                if (query.isNotEmpty() && !labelNames.contains(query)) {
                                    LabelSelectItem(
                                        query = query,
                                        createLabel = {
                                            viewModel.createLabel(query)
                                            viewModel.onQueryChange("")
                                        }
                                    )
                                }
                            }
                            else -> LabelEditItem(
                                focusRequester = createFocusRequester,
                                onCreateLabel = { viewModel.createLabel(it) },
                                labelNames = labelNames
                            )
                        }
                    }
                    items(uiState.labels, key = { it.id }) { label ->
                        when (mode) {
                            is SELECT -> {
                                LabelSelectItem(
                                    label = label,
                                    selectedNotes = uiState.selectedNotes,
                                    updateNotesLabel = { labelId, value ->
                                        viewModel.updateLabelsOfSelectedNotes(labelId, value)
                                    }
                                )
                            }
                            else -> LabelEditItem(
                                label = label,
                                onUpdateLabel = { viewModel.updateLabel(it) },
                                onDeleteLabel = { viewModel.deleteLabel(it) },
                                otherLabelNames = labelNames.filter { it != label.name }
                            )
                        }
                    }
                }
            )
        }
    )

    MyCircularProgressIndicator(isDisplayed = uiState.isLoading)

//    TODO("enable after server is implemented")
//    uiState.errorMessage?.let { errorMessage ->
//        LaunchedEffect(errorMessage) {
//            snackBarHostState.showSnackbar(errorMessage)
//            viewModel.errorMessageShown()
//        }
//    }
}
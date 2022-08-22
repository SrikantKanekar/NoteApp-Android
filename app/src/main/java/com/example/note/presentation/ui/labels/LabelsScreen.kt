package com.example.note.presentation.ui.labels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.note.model.enums.LabelScreenMode
import com.example.note.presentation.components.MyCircularProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelsScreen(
    mode: LabelScreenMode
) {

    val viewModel = hiltViewModel<LabelsViewModel>()
    val uiState = viewModel.uiState

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mode) {
        viewModel.fetchLabels()
    }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Label Screen")
            }
        }
    )

    MyCircularProgressIndicator(isDisplayed = uiState.isLoading)

    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackBarHostState.showSnackbar(errorMessage)
            viewModel.errorMessageShown()
        }
    }
}
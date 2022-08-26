package com.example.note.presentation.ui.labels.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.example.note.model.enums.LabelScreenMode
import com.example.note.presentation.components.MyIconButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LabelsTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    mode: LabelScreenMode,
    query: String,
    onQueryChange: (String) -> Unit,
    navigateBack: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    SmallTopAppBar(
        navigationIcon = {
            MyIconButton(
                icon = Icons.Filled.ArrowBack,
                description = "cancel",
                onClick = navigateBack
            )
        },
        title = {
            when (mode) {
                is LabelScreenMode.SELECT -> {
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Search view" },
                        value = query,
                        onValueChange = onQueryChange,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        ),
                        decorationBox = { innerTextField ->
                            Box {
                                if (query.isEmpty()) {
                                    Text(
                                        modifier = Modifier.alpha(0.5f),
                                        text = "Enter label name",
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                }
                                innerTextField()
                            }
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
                    )
                }
                else -> Text(text = "Edit labels")
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = scrollBehavior
    )
}
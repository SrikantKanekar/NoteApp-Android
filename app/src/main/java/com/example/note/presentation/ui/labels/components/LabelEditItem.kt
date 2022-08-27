package com.example.note.presentation.ui.labels.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.example.note.model.Label
import com.example.note.presentation.components.MyIconButton

@Composable
fun LabelEditItem(
    focusRequester: FocusRequester,
    onCreateLabel: (String) -> Unit,
    labelNames: List<String>
) {
    var labelName by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun handleLabelNameChange(textFieldValue: TextFieldValue) {
        labelName = textFieldValue
        errorMessage = when {
            labelNames.contains(labelName.text) -> "Label already exists"
            else -> ""
        }
    }

    fun reset() {
        errorMessage = ""
        labelName = TextFieldValue("")
    }

    fun hasErrors() = errorMessage.isNotEmpty()

    fun handleFocusChange(value: Boolean) {
        isFocused = value
        if (!isFocused) reset()
    }

    fun handleSaveClick() {
        if (!hasErrors()) {
            onCreateLabel(labelName.text)
            focusManager.clearFocus()
        }
    }

    Column {
        BasicLabelEditItem(
            labelName = labelName,
            onLabelNameChange = { handleLabelNameChange(it) },
            placeholder = "Create new label",
            errorMessage = errorMessage,
            isFocused = isFocused,
            focusRequester = focusRequester,
            onFocusChanged = { handleFocusChange(it) },
            leadingContent = {
                when {
                    isFocused -> MyIconButton(
                        icon = Icons.Filled.Clear,
                        description = "Clear",
                        onClick = { focusManager.clearFocus() }
                    )
                    else -> MyIconButton(
                        icon = Icons.Filled.Add,
                        description = "Create label",
                        onClick = { focusRequester.requestFocus() }
                    )
                }
            },
            trailingContent = {
                if (isFocused) {
                    MyIconButton(
                        icon = Icons.Filled.Check,
                        description = "Save label",
                        onClick = { handleSaveClick() }
                    )
                }
            }
        )
    }
}

@Composable
fun LabelEditItem(
    label: Label,
    onUpdateLabel: (Label) -> Unit,
    onDeleteLabel: (Label) -> Unit,
    otherLabelNames: List<String>
) {
    var labelName by remember { mutableStateOf(TextFieldValue(label.name)) }
    var errorMessage by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun handleLabelNameChange(textFieldValue: TextFieldValue) {
        labelName = textFieldValue
        errorMessage = when {
            labelName.text.isEmpty() -> "Enter a label name"
            otherLabelNames.contains(labelName.text) -> "Label already exists"
            else -> ""
        }
    }

    fun reset() {
        errorMessage = ""
        labelName = TextFieldValue(label.name)
    }

    fun hasErrors() = errorMessage.isNotEmpty()

    fun handleFocusChange(value: Boolean) {
        isFocused = value
        if (!isFocused) {
            when (hasErrors()) {
                true -> reset()
                false -> onUpdateLabel(label.copy(name = labelName.text))
            }
        }
    }

    fun handleSaveClick() {
        if (!hasErrors()) focusManager.clearFocus()
    }

    BasicLabelEditItem(
        labelName = labelName,
        onLabelNameChange = { handleLabelNameChange(it) },
        errorMessage = errorMessage,
        isFocused = isFocused,
        focusRequester = focusRequester,
        onFocusChanged = { handleFocusChange(it) },
        leadingContent = {
            when {
                isFocused -> MyIconButton(
                    icon = Icons.Outlined.Delete,
                    description = "Delete label",
                    onClick = {
                        onDeleteLabel(label)
                        focusManager.clearFocus()
                    }
                )
                else -> MyIconButton(
                    icon = Icons.Outlined.Label,
                    description = "Label",
                    onClick = { }
                )
            }

        },
        trailingContent = {
            when {
                isFocused -> MyIconButton(
                    icon = Icons.Filled.Check,
                    description = "Save label",
                    onClick = { handleSaveClick() }
                )
                else -> MyIconButton(
                    icon = Icons.Filled.Edit,
                    description = "Edit",
                    onClick = { focusRequester.requestFocus() }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasicLabelEditItem(
    labelName: TextFieldValue,
    onLabelNameChange: (TextFieldValue) -> Unit,
    placeholder: String = "",
    errorMessage: String,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    val borderColor = MaterialTheme.colorScheme.onSurfaceVariant

    ListItem(
        modifier = Modifier.drawBehind {
            if (isFocused) {
                val strokeWidth = Stroke.DefaultMiter
                val y1 = size.height - strokeWidth / 2
                val y2 = strokeWidth / 2

                drawLine(
                    borderColor,
                    Offset(0f, y1),
                    Offset(size.width, y1),
                    strokeWidth
                )
                drawLine(
                    borderColor,
                    Offset(0f, y2),
                    Offset(size.width, y2),
                    strokeWidth
                )
            }
        },
        headlineText = {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { onFocusChanged(it.isFocused) },
                value = labelName,
                onValueChange = { onLabelNameChange(it) },
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (placeholder.isNotEmpty()) {
                            if (labelName.text.isEmpty()) {
                                Text(
                                    modifier = Modifier.alpha(0.5f),
                                    text = placeholder,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
            )
        },
        supportingText = {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )
    )
}
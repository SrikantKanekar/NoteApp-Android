package com.example.note.framework.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteBody(
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String) -> Unit
) {
    MyBasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        textStyle = TextStyle(
            color = MaterialTheme.colors.onBackground,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
    )
}

@Composable
fun NoteTitle(
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String) -> Unit
) {
    MyBasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        singleLine = true,
        textStyle = TextStyle(
            color = MaterialTheme.colors.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    )
}


@Composable
fun MySearchView(
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    MyBasicTextField(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        singleLine = true,
        textStyle = TextStyle(
            color = MaterialTheme.colors.onSurface,
            fontSize = 15.sp
        )
    )
}

@Composable
fun MyBasicTextField(
    modifier: Modifier,
    value: String?,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
) {
    BasicTextField(
        modifier = modifier,
        value = value ?: "",
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        textStyle = textStyle,
        cursorBrush = SolidColor(MaterialTheme.colors.onSurface)
    )
}
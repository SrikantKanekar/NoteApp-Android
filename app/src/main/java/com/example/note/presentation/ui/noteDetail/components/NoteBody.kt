package com.example.note.presentation.ui.noteDetail.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.sp
import com.example.note.presentation.components.MyBasicTextField

@Composable
fun NoteBody(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {

    BasicTextField(
        modifier = modifier.semantics { contentDescription = "Note body" },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}
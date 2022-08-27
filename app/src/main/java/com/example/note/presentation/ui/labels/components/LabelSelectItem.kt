package com.example.note.presentation.ui.labels.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.IndeterminateCheckBox
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.example.note.model.Label
import com.example.note.model.Note
import com.example.note.presentation.components.MyIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelSelectItem(
    label: Label,
    selectedNotes: List<Note>,
    updateNotesLabel: (String, Boolean) -> Unit
) {
    ListItem(
        headlineText = {
            Text(
                text = label.name,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            MyIconButton(
                icon = Icons.Outlined.Label,
                description = "Label",
                onClick = { }
            )
        },
        trailingContent = {
            when {
                selectedNotes.all { it.labels.contains(label.id) } -> MyIconButton(
                    icon = Icons.Filled.CheckBox,
                    description = "remove label",
                    onClick = { updateNotesLabel(label.id, false) }
                )
                selectedNotes.any { it.labels.contains(label.id) } -> MyIconButton(
                    icon = Icons.Outlined.IndeterminateCheckBox,
                    description = "add label for all",
                    onClick = { updateNotesLabel(label.id, true) }
                )
                else -> MyIconButton(
                    icon = Icons.Outlined.CheckBoxOutlineBlank,
                    description = "add label",
                    onClick = { updateNotesLabel(label.id, true) }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelSelectItem(
    query: String,
    createLabel: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { createLabel() },
        leadingContent = {
            MyIconButton(
                icon = Icons.Filled.Add,
                description = "Create label",
                onClick = createLabel
            )
        },
        headlineText = {
            Text(
                text = "Create \"$query\"",
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}
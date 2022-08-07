package com.example.note.presentation.ui.notes.components.dropdownMenu

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.note.presentation.components.MyDropdownMenuItem
import com.example.note.presentation.components.MyIconButton

@Composable
fun DeleteDropdownMenu(
    onEmptyRecycleBin: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {

        MyIconButton(
            icon = Icons.Outlined.MoreVert,
            description = "More options",
            onClick = { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(0.dp, (-50).dp),
            onDismissRequest = { expanded = false }
        ) {

            MyDropdownMenuItem(
                text = "Empty Recycle Bin",
                onClick = {
                    expanded = false
                    onEmptyRecycleBin()
                }
            )
        }
    }
}
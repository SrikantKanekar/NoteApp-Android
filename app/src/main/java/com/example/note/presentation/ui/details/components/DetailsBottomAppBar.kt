package com.example.note.presentation.ui.details.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailsBottomAppBar(
    updatedAt: String?,
    onAddClick: () -> Unit,
    onColorClick: () -> Unit,
    onMoreClick: () -> Unit,
) {
    BottomAppBar(
        modifier = Modifier.height(55.dp),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    IconButton(
                        onClick = onAddClick
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddBox,
                            contentDescription = "AddBox icon"
                        )
                    }
                    IconButton(
                        onClick = onColorClick
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = "Palette icon"
                        )
                    }
                }

                Text(
                    text = "Edited $updatedAt",
                    style = MaterialTheme.typography.labelMedium
                )

                IconButton(
                    onClick = onMoreClick
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "MoreVert icon"
                    )
                }
            }
        }
    )
}
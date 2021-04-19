package com.example.note.framework.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.DismissDirection.*
import androidx.compose.material.DismissValue.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.note.business.domain.model.Note

@ExperimentalMaterialApi
@Composable
fun SwipeNoteCard(
    note: Note,
    onClick: (String) -> Unit,
    dismissedToStart: (String) -> Unit,
    dismissedToEnd: (String) -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            when (dismissValue) {
                DismissedToStart -> {
                    dismissedToStart(note.id)
                    false
                }
                DismissedToEnd -> {
                    dismissedToEnd(note.id)
                    false
                }
                Default -> { true }
            }
        }
    )

    SwipeToDismiss(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(8.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable { onClick(note.id) },
        state = dismissState,
        dismissThresholds = { FractionalThreshold(0.5f) },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss

            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    Default -> Color.Black
                    DismissedToStart -> MaterialTheme.colors.primary
                    DismissedToEnd -> MaterialTheme.colors.error
                }
            )
            val alignment = when (direction) {
                EndToStart -> Alignment.CenterEnd
                StartToEnd -> Alignment.CenterStart
            }
            val icon = when (direction) {
                EndToStart -> Icons.Default.Archive
                StartToEnd -> Icons.Default.Delete
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == Default) 0.75f else 1f
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    modifier = Modifier.scale(scale),
                    imageVector = icon,
                    contentDescription = null
                )
            }
        },
        dismissContent = {
            Card(
                backgroundColor = MaterialTheme.colors.surface,
                elevation = animateDpAsState(
                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                ).value
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.h5
                    )
                    Text(text = note.id)
                }
            }
        }
    )
}
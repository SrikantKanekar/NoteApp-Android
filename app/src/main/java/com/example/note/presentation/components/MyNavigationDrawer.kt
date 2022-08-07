package com.example.note.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.note.model.Label
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val NOTES = "Notes"
private const val REMINDERS = "Reminders"
private const val LABEL_CREATE = "Create new label"
private const val ARCHIVE = "Archive"
private const val DELETED = "Deleted"
private const val SETTINGS = "Settings"
private const val HELP = "Help & feedback"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNavigationDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    labels: List<Label>,
    navigateToNotes: () -> Unit,
    navigateToReminders: () -> Unit,
    navigateToLabels: (String) -> Unit,
    navigateToLabelEdit: (String) -> Unit,
    navigateToArchive: () -> Unit,
    navigateToDeleted: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToHelp: () -> Unit,
    content: @Composable () -> Unit
) {
    val selectedItem = remember { mutableStateOf(NOTES) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            Text(
                modifier = Modifier.padding(26.dp),
                text = "Note App",
                style = MaterialTheme.typography.titleLarge
            )

            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = { Icon(Icons.Outlined.Lightbulb, contentDescription = null) },
                label = { Text(NOTES) },
                selected = selectedItem.value == NOTES,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = NOTES
                    navigateToNotes()
                }
            )

            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = { Icon(Icons.Outlined.NotificationsNone, contentDescription = null) },
                label = { Text(REMINDERS) },
                selected = selectedItem.value == REMINDERS,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = REMINDERS
                    navigateToReminders()
                }
            )

            if (labels.isNotEmpty()) Divider(Modifier.padding(vertical = 8.dp))

            if (labels.isNotEmpty()) {
                NavigationDrawerItem(
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Labels",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { navigateToLabelEdit("editLabel") }
                                    .padding(vertical = 12.dp, horizontal = 20.dp),
                                text = "Edit",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    },
                    selected = false,
                    onClick = { }
                )
            }

            for (label in labels) {
                NavigationDrawerItem(
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    icon = { Icon(Icons.Outlined.Label, contentDescription = null) },
                    label = { Text(label.name) },
                    selected = selectedItem.value == label.id,
                    onClick = {
                        scope.launch { drawerState.close() }
                        selectedItem.value = label.id
                        navigateToLabels(label.id)
                    }
                )
            }

            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                label = { Text(LABEL_CREATE) },
                selected = false,
                onClick = {
                    scope.launch { drawerState.close() }
                    navigateToLabelEdit("addLabel")
                }
            )

            if (labels.isNotEmpty()) Divider(Modifier.padding(vertical = 8.dp))

            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = { Icon(Icons.Outlined.Archive, contentDescription = null) },
                label = { Text(ARCHIVE) },
                selected = selectedItem.value == ARCHIVE,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = ARCHIVE
                    navigateToArchive()
                }
            )

            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                label = { Text(DELETED) },
                selected = selectedItem.value == DELETED,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = DELETED
                    navigateToDeleted()
                }
            )

            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                label = { Text(SETTINGS) },
                selected = selectedItem.value == SETTINGS,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = SETTINGS
                    navigateToSettings()
                }
            )

            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = { Icon(Icons.Outlined.HelpOutline, contentDescription = null) },
                label = { Text(HELP) },
                selected = selectedItem.value == HELP,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = HELP
                    navigateToHelp()
                }
            )
        },
        content = content
    )

    BackHandler(
        enabled = drawerState.isOpen
    ) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        }
    }
}
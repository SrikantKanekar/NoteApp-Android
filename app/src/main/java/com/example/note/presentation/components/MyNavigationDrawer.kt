package com.example.note.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.note.presentation.navigation.Navigation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val NOTES = "Notes"
private const val REMINDERS = "Reminders"
private const val ARCHIVE = "Archive"
private const val DELETED = "Deleted"
private const val SETTINGS = "Settings"
private const val HELP = "Help & feedback"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNavigationDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController,
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
                    navController.navigate(Notes.route) {
                        popUpTo(0)
                    }
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
                    navController.navigate(Reminders.route) {
                        popUpTo(0)
                    }
                }
            )

            Divider(Modifier.padding(vertical = 8.dp))

            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = { Icon(Icons.Outlined.Archive, contentDescription = null) },
                label = { Text(ARCHIVE) },
                selected = selectedItem.value == ARCHIVE,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = ARCHIVE
                    navController.navigate(Archive.route) {
                        popUpTo(0)
                    }
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
                    navController.navigate(Deleted.route) {
                        popUpTo(0)
                    }
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
                    navController.navigate(Settings.route)
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
                    navController.navigate(HelpAndFeedback.route)
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
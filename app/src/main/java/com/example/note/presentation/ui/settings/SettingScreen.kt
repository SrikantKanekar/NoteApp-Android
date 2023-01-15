package com.example.note.presentation.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.note.SettingPreferences.Theme.DARK
import com.example.note.model.Setting
import com.example.note.presentation.components.MyIconButton
import com.example.note.presentation.components.SettingSwitch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navigateBack: () -> Unit
) {

    val settingsViewModel = hiltViewModel<SettingViewModel>()
    val settings = settingsViewModel.settingFlow.collectAsState(initial = Setting())

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    MyIconButton(
                        icon = Icons.Filled.ArrowBack,
                        description = "back",
                        onClick = navigateBack
                    )
                },
                title = { Text(text = "Settings") },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SettingSwitch(
                imageVector = Icons.Default.ColorLens,
                text = "Theme",
                textValue = settings.value.theme.name.lowercase(Locale.ROOT),
                checked = settings.value.theme == DARK,
                onCheckedChange = { theme ->
                    settingsViewModel.setTheme(theme)
                },
                switchContentDescription = "theme switch"
            )
        }
    }
}
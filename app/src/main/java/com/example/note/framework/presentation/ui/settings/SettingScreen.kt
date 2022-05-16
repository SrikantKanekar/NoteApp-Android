package com.example.note.framework.presentation.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.note.SettingPreferences.Theme
import com.example.note.SettingPreferences.Theme.DARK
import com.example.note.SettingPreferences.Theme.LIGHT
import com.example.note.business.domain.model.Setting
import com.example.note.framework.presentation.theme.AppTheme
import java.util.*

@Composable
fun SettingScreen(
    theme: Theme,
    scaffoldState: ScaffoldState
) {

    AppTheme(
        theme = theme,
        scaffoldState = scaffoldState,
        stateMessage = null,
        removeStateMessage = { }
    ) {

        val settingsViewModel = hiltViewModel<SettingViewModel>()
        val settings = settingsViewModel.settingFlow.collectAsState(initial = Setting())

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .padding(top = 25.dp)
            ) {

                SwitchSetting(
                    imageVector = Icons.Default.ColorLens,
                    theme = settings.value.theme,
                    value = settings.value.theme.name,
                    onCheckedChange = { theme ->
                        settingsViewModel.setTheme(theme)
                    }
                )
            }
        }
    }
}

@Composable
fun SwitchSetting(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    theme: Theme,
    value: String,
    onCheckedChange: (Theme) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(5.dp),
                imageVector = imageVector,
                contentDescription = null
            )

            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Text(
                    text = "Theme",
                    fontSize = 15.sp
                )
                Text(
                    text = value.lowercase(Locale.ROOT),
                    fontSize = 13.sp
                )
            }
        }

        Switch(
            modifier = Modifier.padding(5.dp),
            checked = theme == DARK,
            onCheckedChange = { isDark ->
                onCheckedChange(if (isDark) DARK else LIGHT)
            }
        )
    }
}
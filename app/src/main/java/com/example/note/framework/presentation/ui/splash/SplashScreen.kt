package com.example.note.framework.presentation.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.note.SettingPreferences.Theme
import com.example.note.framework.presentation.components.AppLogo
import com.example.note.framework.presentation.navigation.Navigation.NoteList
import com.example.note.framework.presentation.navigation.Navigation.Splash
import com.example.note.framework.presentation.theme.AppTheme

@Composable
fun SplashScreen(
    theme: Theme,
    navController: NavHostController
) {

    val viewModel = hiltViewModel<SplashViewModel>()
    val hasSyncBeenExecuted = viewModel.hasSyncBeenExecuted.collectAsState()

    if (hasSyncBeenExecuted.value) {
        LaunchedEffect(Unit) {
            navController.navigate(NoteList.route) {
                popUpTo(Splash.route) {
                    inclusive = true
                }
            }
        }
    }

    AppTheme(
        theme = theme
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AppLogo()
        }
    }
}
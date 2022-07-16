package com.example.note.presentation.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.note.SettingPreferences.Theme
import com.example.note.presentation.components.AppLogo
import com.example.note.presentation.navigation.Navigation.NoteList
import com.example.note.presentation.navigation.Navigation.Splash
import com.example.note.presentation.theme.AppTheme

@Composable
fun SplashScreen(
    theme: Theme,
    navController: NavHostController
) {

    val viewModel = hiltViewModel<SplashViewModel>()

    if (viewModel.synced) {
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
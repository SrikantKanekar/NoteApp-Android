package com.example.note.presentation.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.note.presentation.navigation.Navigation
import com.example.note.presentation.navigation.Navigation.*

@Composable
fun SplashScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<SplashViewModel>()

    if (viewModel.synced) {
        LaunchedEffect(Unit) {
            navController.navigate(Main.route) {
                popUpTo(Splash.route) {
                    inclusive = true
                }
            }
        }
    }
}
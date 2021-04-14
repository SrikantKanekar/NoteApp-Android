package com.example.note.framework.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.note.R

@Composable
fun AppLogo() {

    val painterResource = painterResource(R.drawable.logo_foreground)

    Image(
        painter = painterResource,
        contentDescription = "Splash logo"
    )
}
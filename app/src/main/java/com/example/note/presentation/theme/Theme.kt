package com.example.note.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.note.SettingPreferences.Theme
import com.example.note.SettingPreferences.Theme.DARK

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = blue500,
    primaryVariant = blue800,
    secondary = blue500,
    secondaryVariant = blue800,
    background = Color.White,
    surface = Color.White,
    error = red500,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

private val DarkColorPalette = darkColors(
    primary = blue300,
    primaryVariant = blue800,
    secondary = blue300,
    background = darkBackground,
    surface = darkSurface,
    error = red300,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

@Composable
fun AppTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    val colour = if (theme == DARK) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colour,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
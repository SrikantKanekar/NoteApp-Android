package com.example.note.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import com.example.note.SettingPreferences.Theme
import com.example.note.SettingPreferences.Theme.DARK

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
//    background = darkBackground,
//    surface = darkSurface,
//    error = red300,
//    onPrimary = Color.Black,
//    onSecondary = Color.Black,
//    onBackground = Color.White,
//    onSurface = Color.White,
//    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
//    background = Color.White,
//    surface = Color.White,
//    error = red500,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//    onError = Color.White
)

@Composable
fun AppTheme(
    theme: Theme,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val isDark = theme == DARK

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
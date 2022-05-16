package com.example.note.framework.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.note.SettingPreferences.*
import com.example.note.SettingPreferences.Theme.*
import com.example.note.business.domain.state.StateMessage
import com.example.note.framework.presentation.components.MyCircularProgressIndicator
import com.example.note.framework.presentation.components.snackbar.SnackBarController
import com.example.note.framework.presentation.components.stateMessageHandler.HandleMessageUiType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main

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

val snackBarController = SnackBarController(CoroutineScope(Main))

@Composable
fun AppTheme(
    theme: Theme,
    displayProgressBar: Boolean = false,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    stateMessage: StateMessage? = null,
    removeStateMessage: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val colour = if (theme == DARK) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colour,
        typography = typography,
        shapes = shapes
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            content()

            MyCircularProgressIndicator(
                isDisplayed = displayProgressBar,
                modifier = Modifier.align(Alignment.Center)
            )

            HandleMessageUiType(
                stateMessage = stateMessage,
                scaffoldState = scaffoldState,
                removeStateMessage = removeStateMessage
            )
        }
    }
}
package com.example.note.framework.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.note.R

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 110.dp
) {

    val painterResource = painterResource(R.mipmap.ic_launcher_round)

    Image(
        modifier = modifier.requiredSize(size),
        painter = painterResource,
        contentDescription = "Logo"
    )
}
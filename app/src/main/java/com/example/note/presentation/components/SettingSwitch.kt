package com.example.note.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.note.SettingPreferences

@Composable
fun SettingSwitch(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    text: String,
    textValue: String,
    checked: Boolean,
    onCheckedChange: (SettingPreferences.Theme) -> Unit,
    switchContentDescription: String
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
                    text = text,
                    fontSize = 15.sp
                )
                Text(
                    text = textValue,
                    fontSize = 13.sp
                )
            }
        }

        Switch(
            modifier = Modifier
                .padding(5.dp)
                .semantics { contentDescription = switchContentDescription },
            checked = checked,
            onCheckedChange = { isDark ->
                onCheckedChange(if (isDark) SettingPreferences.Theme.DARK else SettingPreferences.Theme.LIGHT)
            }
        )
    }
}
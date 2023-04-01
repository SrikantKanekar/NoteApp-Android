package com.example.note.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.note.SettingPreferences.Theme
import com.example.note.cache.dataStore.SettingDataStore
import com.example.note.presentation.navigation.NavigationGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingDataStore: SettingDataStore

    private val appTheme = mutableStateOf(Theme.DARK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingDataStore.settingFlow.collect { setting ->
                    appTheme.value = setting.theme
                }
            }
        }

        setContent {
            NavigationGraph(theme = appTheme.value)
        }
    }
}
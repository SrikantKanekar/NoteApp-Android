package com.example.note.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import com.example.note.SettingPreferences.Theme
import com.example.note.cache.dataStore.SettingDataStore
import com.example.note.presentation.navigation.NavigationGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingDataStore: SettingDataStore

    private val appTheme = mutableStateOf(Theme.DARK)

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            settingDataStore.settingFlow.collect { setting ->
                appTheme.value = setting.theme
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationGraph(theme = appTheme.value)
        }
    }
}
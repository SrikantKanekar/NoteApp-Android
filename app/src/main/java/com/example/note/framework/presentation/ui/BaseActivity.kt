package com.example.note.framework.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import com.example.faircon.SettingPreferences.Theme.*
import com.example.note.framework.datasource.datastore.SettingDataStore
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var settingDataStore: SettingDataStore

    val appTheme = mutableStateOf(DARK)

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            settingDataStore.settingFlow.collect { setting ->
                appTheme.value = setting.theme
            }
        }
    }
}
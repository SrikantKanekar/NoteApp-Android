package com.example.note.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.SettingPreferences.Theme
import com.example.note.cache.dataStore.SettingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingDataStore: SettingDataStore
) : ViewModel() {

    val settingFlow = settingDataStore.settingFlow

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            settingDataStore.updateTheme(theme)
        }
    }
}
package com.example.note.business.domain.model

import com.example.note.SettingPreferences.Theme

data class Setting(
    val theme: Theme = Theme.DARK
)

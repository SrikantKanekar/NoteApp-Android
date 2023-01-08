package com.example.note.cache.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.note.SettingPreferences
import com.example.note.UserPreferences

val Context.userDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "USER_DATASTORE_FILE",
    serializer = UserSerializer
)

val Context.settingDataStore: DataStore<SettingPreferences> by dataStore(
    fileName = "SETTING_DATASTORE_FILE",
    serializer = SettingSerializer
)
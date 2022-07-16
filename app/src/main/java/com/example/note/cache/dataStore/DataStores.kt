package com.example.note.cache.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.note.AccountPreferences
import com.example.note.SettingPreferences

val Context.accountDataStore: DataStore<AccountPreferences> by dataStore(
    fileName = "ACCOUNT_DATASTORE_FILE",
    serializer = AccountSerializer
)

val Context.settingDataStore: DataStore<SettingPreferences> by dataStore(
    fileName = "SETTING_DATASTORE_FILE",
    serializer = SettingSerializer
)
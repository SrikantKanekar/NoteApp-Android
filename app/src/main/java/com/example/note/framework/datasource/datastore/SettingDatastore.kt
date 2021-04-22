package com.example.note.framework.datasource.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.faircon.SettingPreferences
import com.example.faircon.SettingPreferences.*
import com.example.note.business.domain.model.Setting
import com.example.note.framework.presentation.ui.BaseApplication
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

class SettingDataStore(private val application: BaseApplication) {

    private val Context.settingDataStore: DataStore<SettingPreferences> by dataStore(
        fileName = Files.SETTING_DATASTORE_FILE,
        serializer = SettingSerializer
    )

    val settingFlow: Flow<Setting> = application.settingDataStore.data
        .map { preferences ->
            Setting(theme = preferences.theme)
        }

    suspend fun updateTheme(theme: Theme) {
        withContext(IO) {
            application.settingDataStore.updateData { settingPreferences ->
                settingPreferences.toBuilder()
                    .setTheme(theme)
                    .build()
            }
        }
    }
}

object SettingSerializer : Serializer<SettingPreferences> {

    override val defaultValue: SettingPreferences =
        newBuilder()
            .setTheme(Theme.DARK)
            .build()


    override suspend fun readFrom(input: InputStream): SettingPreferences {
        try {
            return parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override suspend fun writeTo(t: SettingPreferences, output: OutputStream) {
        return t.writeTo(output)
    }
}
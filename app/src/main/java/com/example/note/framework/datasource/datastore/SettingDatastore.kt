package com.example.note.framework.datasource.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.note.SettingPreferences
import com.example.note.SettingPreferences.*
import com.example.note.business.domain.model.Setting
import com.google.protobuf.InvalidProtocolBufferException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val settingFlow: Flow<Setting> = context.settingDataStore.data
        .map { preferences ->
            Setting(theme = preferences.theme)
        }

    suspend fun updateTheme(theme: Theme) {
        withContext(IO) {
            context.settingDataStore.updateData { settingPreferences ->
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
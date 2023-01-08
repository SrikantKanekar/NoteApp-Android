package com.example.note.cache.dataStore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.note.UserPreferences
import com.example.note.model.User
import com.google.protobuf.InvalidProtocolBufferException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDatastore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val userFlow: Flow<User> = context.userDataStore.data
        .map { preferences ->
            User(preferences.email, preferences.token, preferences.username, preferences.isAdmin)
        }

    fun get() = runBlocking {
        val preferences = context.userDataStore.data.first()
        User(preferences.email, preferences.token, preferences.username, preferences.isAdmin)
    }

    suspend fun updateUser(user: User) {
        context.userDataStore.updateData { preferences ->
            preferences.toBuilder()
                .setEmail(user.email)
                .setToken(user.token)
                .setUsername(user.username)
                .setIsAdmin(user.isAdmin)
                .build()
        }
    }
}

object UserSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences =
        UserPreferences.newBuilder()
            .setEmail("user1@email.com")
            .setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJOb3RlIGFwcHMiLCJpc3MiOiJOb3RlU2VydmVyIiwiaXNBZG1pbiI6ZmFsc2UsImVtYWlsIjoidXNlcjFAZW1haWwuY29tIiwidXNlcm5hbWUiOiJ1c2VybmFtZTEifQ.1CJnO5jyjLo4kZFnp3Bbj2pEjCWnfoBfCSb6diAFz-Q")
            .setUsername("username1")
            .setIsAdmin(false)
            .build()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        return t.writeTo(output)
    }
}
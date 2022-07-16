package com.example.note.cache.dataStore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.note.AccountPreferences
import com.example.note.model.Account
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
class AccountDatastore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val accountFlow: Flow<Account> = context.accountDataStore.data
        .map { preferences ->
            Account(preferences.email, preferences.password)
        }

    fun get() = runBlocking {
        val account = context.accountDataStore.data.first()
        Account(account.email, account.password)
    }

    suspend fun updateAccount(account: Account) {
        context.accountDataStore.updateData { accountPreferences ->
            accountPreferences.toBuilder()
                .setEmail(account.email)
                .setPassword(account.password)
                .build()
        }
    }
}

object AccountSerializer : Serializer<AccountPreferences> {
    override val defaultValue: AccountPreferences =
        AccountPreferences.newBuilder()
            .setEmail("TestUser")
            .setPassword("password")
            .build()

    override suspend fun readFrom(input: InputStream): AccountPreferences {
        try {
            return AccountPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override suspend fun writeTo(t: AccountPreferences, output: OutputStream) {
        return t.writeTo(output)
    }
}
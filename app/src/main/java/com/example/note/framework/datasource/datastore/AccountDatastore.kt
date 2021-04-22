package com.example.note.framework.datasource.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.faircon.AccountPreferences
import com.example.note.business.domain.model.Account
import com.example.note.framework.presentation.ui.BaseApplication
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream

class AccountDatastore(private val application: BaseApplication) {

    private val Context.accountDataStore: DataStore<AccountPreferences> by dataStore(
        fileName = Files.ACCOUNT_DATASTORE_FILE,
        serializer = AccountSerializer
    )

    val accountFlow: Flow<Account> = application.accountDataStore.data
        .map { preferences ->
            Account(preferences.email, preferences.password)
        }

    fun get() = runBlocking {
        val account = application.accountDataStore.data.first()
        Account(account.email, account.password)
    }

    suspend fun updateAccount(account: Account) {
        application.accountDataStore.updateData { accountPreferences ->
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
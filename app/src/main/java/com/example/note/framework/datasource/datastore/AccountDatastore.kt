package com.example.note.framework.datasource.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.example.faircon.AccountPreferences
import com.example.note.business.domain.model.Account
import com.example.note.framework.presentation.ui.BaseApplication
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class AccountDatastore(application: BaseApplication) {

    private val dataStore = application
        .createDataStore(Files.ACCOUNT_DATASTORE_FILE, AccountSerializer)

    private val default: AccountPreferences = AccountPreferences.newBuilder()
        .setEmail("TestUser")
        .setPassword("password")
        .build()

    val accountFlow: Flow<Account> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(default)
            } else {
                throw exception
            }
        }
        .map {
            Account(it.email, it.password)
        }

    fun get(): Account = runBlocking {
        val account = try {
            dataStore.data.first()
        } catch (e: Exception) {
            e.printStackTrace()
            default
        }
        Account(account.email, account.password)
    }

    suspend fun updateAccount(account: Account) {
        dataStore.updateData { accountPreferences ->
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

    override fun readFrom(input: InputStream): AccountPreferences {
        try {
            return AccountPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override fun writeTo(t: AccountPreferences, output: OutputStream) {
        return t.writeTo(output)
    }
}
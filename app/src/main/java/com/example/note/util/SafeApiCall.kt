package com.example.note.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T?
): T? {
    return try {
        apiCall(dispatcher) {
            call.invoke()
        }
    } catch (e: Exception) {
        return null
    }
}

suspend fun <T> apiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T?
): T? {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                call.invoke()
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            val message = when (throwable) {
                is TimeoutCancellationException -> NETWORK_ERROR_TIMEOUT
                is IOException -> NETWORK_ERROR
                is HttpException -> throw throwable
                else -> NETWORK_ERROR_UNKNOWN
            }
            throw Exception(message)
        }
    }
}
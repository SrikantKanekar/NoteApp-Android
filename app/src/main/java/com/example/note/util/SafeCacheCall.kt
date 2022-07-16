package com.example.note.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T?
): T? {
    return try {
        cacheCall(dispatcher) {
            call.invoke()
        }
    } catch (e: Exception) {
        return null
    }
}

suspend fun <T> cacheCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T?
): T? {
    return withContext(dispatcher) {
        try {
            withTimeout(CACHE_TIMEOUT) {
                call.invoke()
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()

            val message = when (throwable) {
                is TimeoutCancellationException -> CACHE_ERROR_TIMEOUT
                else -> CACHE_ERROR_UNKNOWN
            }
            throw Exception(message)
        }
    }
}
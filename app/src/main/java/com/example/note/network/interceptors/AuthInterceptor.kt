package com.example.note.network.interceptors

import com.example.note.cache.dataStore.UserDatastore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val userDatastore: UserDatastore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val user = userDatastore.get()
        val request = chain.request()
        val authenticatedRequest = request
            .newBuilder()
            .header(
                "Authorization",
                "Bearer ${user.token}"
            ).build()
        return chain.proceed(authenticatedRequest)
    }
}
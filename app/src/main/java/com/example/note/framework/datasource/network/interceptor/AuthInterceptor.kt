package com.example.note.framework.datasource.network.interceptor

import com.example.note.framework.datasource.datastore.AccountDatastore
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val accountDatastore: AccountDatastore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val account = accountDatastore.get()
        val request = chain.request()
        val authenticatedRequest = request
            .newBuilder()
            .header(
                "Authorization",
                Credentials.basic(
                    account.email,
                    account.password
                )
            ).build()
        return chain.proceed(authenticatedRequest)
    }
}
package com.example.note.di

import com.example.note.business.domain.util.Urls.Companion.BASE_URL
import com.example.note.framework.datasource.network.api.AuthApi
import com.example.note.framework.datasource.network.api.DeletedNotesApi
import com.example.note.framework.datasource.network.api.NoteApi
import com.example.note.framework.datasource.network.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideAuthOkHttp(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @AuthRetrofit
    @Singleton
    @Provides
    fun provideAuthRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @DefaultRetrofit
    @Singleton
    @Provides
    fun provideDefaultRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthApi(
        @DefaultRetrofit retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNoteApi(
        @AuthRetrofit retrofit: Retrofit
    ): NoteApi {
        return retrofit.create(NoteApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDeletedNotesApi(
        @AuthRetrofit retrofit: Retrofit
    ): DeletedNotesApi {
        return retrofit.create(DeletedNotesApi::class.java)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultRetrofit
}

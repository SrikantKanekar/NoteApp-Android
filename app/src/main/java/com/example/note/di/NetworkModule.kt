package com.example.note.di

import com.example.note.network.api.AuthApi
import com.example.note.network.api.LabelApi
import com.example.note.network.api.NoteApi
import com.example.note.network.interceptors.AuthInterceptor
import com.example.note.util.BASE_URL
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
    fun provideLabelApi(
        @AuthRetrofit retrofit: Retrofit
    ): LabelApi {
        return retrofit.create(LabelApi::class.java)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultRetrofit
}

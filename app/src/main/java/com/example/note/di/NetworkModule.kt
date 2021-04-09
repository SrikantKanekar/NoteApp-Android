package com.example.note.di

import com.example.note.business.domain.util.Urls
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

//    @Provides
//    fun provideConnectService(
//        retrofit: Retrofit
//    ): ConnectService {
//        return retrofit
//            .create(ConnectService::class.java)
//    }
}
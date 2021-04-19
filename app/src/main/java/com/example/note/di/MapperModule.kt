package com.example.note.di

import com.example.note.framework.datasource.cache.NoteCacheMapper
import com.example.note.framework.datasource.network.mapper.NoteDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Singleton
    @Provides
    fun provideNoteCacheMapper() = NoteCacheMapper()

    @Singleton
    @Provides
    fun provideNoteDtoMapper() = NoteDtoMapper()
}
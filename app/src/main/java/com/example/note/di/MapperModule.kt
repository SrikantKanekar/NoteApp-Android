package com.example.note.di

import com.example.note.framework.datasource.cache.NoteCacheMapper
import com.example.note.framework.datasource.network.mapper.NoteDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    fun provideNoteCacheMapper() = NoteCacheMapper()

    @Provides
    fun provideNoteDtoMapper() = NoteDtoMapper()
}
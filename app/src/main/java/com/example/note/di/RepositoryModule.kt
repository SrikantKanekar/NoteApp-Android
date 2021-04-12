package com.example.note.di

import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.business.data.cache.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideNoteRepository(
        noteCacheDataSource: NoteCacheDataSource
    ): NoteRepository{
        return NoteRepository(noteCacheDataSource)
    }
}
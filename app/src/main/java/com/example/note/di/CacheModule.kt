package com.example.note.di

import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.framework.datasource.cache.NoteCacheMapper
import com.example.note.framework.datasource.cache.NoteCacheService
import com.example.note.framework.datasource.cache.NoteDao
import com.example.note.framework.datasource.cache.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.noteDao()
    }

    @Singleton
    @Provides
    fun provideNoteCacheDataSource(
        noteDao: NoteDao,
        noteCacheMapper: NoteCacheMapper
    ): NoteCacheDataSource {
        return NoteCacheService(noteDao, noteCacheMapper)
    }
}
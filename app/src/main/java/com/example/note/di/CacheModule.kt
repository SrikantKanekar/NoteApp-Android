package com.example.note.di

import androidx.room.Room
import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.framework.datasource.cache.NoteCacheMapper
import com.example.note.framework.datasource.cache.NoteCacheService
import com.example.note.framework.datasource.cache.NoteDao
import com.example.note.framework.datasource.cache.NoteDatabase
import com.example.note.framework.presentation.ui.BaseApplication
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
    fun provideNoteDatabase(app: BaseApplication): NoteDatabase {
        return Room
            .databaseBuilder(app, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

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
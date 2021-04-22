package com.example.note.di

import androidx.room.Room
import com.example.note.framework.datasource.cache.NoteDatabase
import com.example.note.framework.presentation.ui.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductionModule {

    @Singleton
    @Provides
    fun provideNoteDatabase(application: BaseApplication): NoteDatabase {
        return Room
            .databaseBuilder(application, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
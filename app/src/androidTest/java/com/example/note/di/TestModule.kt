package com.example.note.di

import android.app.Application
import androidx.room.Room
import com.example.note.framework.datasource.cache.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProductionModule::class]
)
object TestModule {

    @Singleton
    @Provides
    fun provideNoteDatabase(application: Application): NoteDatabase {
        return Room
            .inMemoryDatabaseBuilder(application, NoteDatabase::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }
}
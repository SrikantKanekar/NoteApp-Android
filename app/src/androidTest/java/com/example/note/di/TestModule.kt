package com.example.note.di

import android.content.Context
import androidx.room.Room
import com.example.note.cache.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideNoteDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase {
        return Room
            .inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }
}
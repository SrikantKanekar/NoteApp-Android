package com.example.note.di

import com.example.note.repository.LabelRepository
import com.example.note.repository.LabelRepositoryImpl
import com.example.note.repository.NoteRepository
import com.example.note.repository.NoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ): NoteRepository

    @Singleton
    @Binds
    abstract fun bindLabelRepository(
        labelRepositoryImpl: LabelRepositoryImpl
    ): LabelRepository
}
package com.example.note.di

import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.DeletedNotesNetworkDataSource
import com.example.note.business.data.network.NoteNetworkDataSource
import com.example.note.business.data.network.NoteNetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNoteCacheRepository(
        noteCacheDataSource: NoteCacheDataSource
    ): NoteCacheRepository {
        return NoteCacheRepository(noteCacheDataSource)
    }

    @Singleton
    @Provides
    fun provideNoteNetworkRepository(
        noteNetworkDataSource: NoteNetworkDataSource,
        deletedNotesNetworkDataSource: DeletedNotesNetworkDataSource
    ): NoteNetworkRepository {
        return NoteNetworkRepository(noteNetworkDataSource, deletedNotesNetworkDataSource)
    }
}
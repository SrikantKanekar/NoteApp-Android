package com.example.note.di

import com.example.note.business.data.cache.NoteCacheDataSource
import com.example.note.business.data.network.DeletedNotesNetworkDataSource
import com.example.note.business.data.network.NoteNetworkDataSource
import com.example.note.framework.datasource.cache.NoteCacheService
import com.example.note.framework.datasource.network.service.DeletedNotesNetworkService
import com.example.note.framework.datasource.network.service.NoteNetworkService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNoteCacheDataSource(
        noteCacheService: NoteCacheService
    ): NoteCacheDataSource

    @Singleton
    @Binds
    abstract fun bindNoteNetworkDataSource(
        noteNetworkService: NoteNetworkService
    ): NoteNetworkDataSource

    @Singleton
    @Binds
    abstract fun bindDeletedNotesNetworkDataSource(
        deletedNotesNetworkService: DeletedNotesNetworkService
    ): DeletedNotesNetworkDataSource
}
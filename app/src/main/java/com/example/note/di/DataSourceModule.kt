package com.example.note.di

import com.example.note.cache.database.dataSource.NoteCacheDataSource
import com.example.note.cache.database.dataSource.NoteCacheDataSourceImpl
import com.example.note.network.dataSource.NoteNetworkDataSource
import com.example.note.network.dataSource.NoteNetworkDataSourceImpl
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
    abstract fun bindNoteLocalDataSource(
        noteLocalDataSourceImpl: NoteCacheDataSourceImpl
    ): NoteCacheDataSource

    @Singleton
    @Binds
    abstract fun bindNoteRemoteDataSource(
        noteRemoteDataSourceImpl: NoteNetworkDataSourceImpl
    ): NoteNetworkDataSource
}
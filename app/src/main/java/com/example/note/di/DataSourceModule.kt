package com.example.note.di

import com.example.note.cache.database.dataSource.LabelCacheDataSource
import com.example.note.cache.database.dataSource.LabelCacheDataSourceImpl
import com.example.note.cache.database.dataSource.NoteCacheDataSource
import com.example.note.cache.database.dataSource.NoteCacheDataSourceImpl
import com.example.note.network.dataSource.LabelNetworkDataSource
import com.example.note.network.dataSource.LabelNetworkDataSourceImpl
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
    abstract fun bindNoteCacheDataSource(
        noteCacheDataSourceImpl: NoteCacheDataSourceImpl
    ): NoteCacheDataSource

    @Singleton
    @Binds
    abstract fun bindLabelCacheDataSource(
        labelCacheDataSourceImpl: LabelCacheDataSourceImpl
    ): LabelCacheDataSource

    @Singleton
    @Binds
    abstract fun bindNoteNetworkDataSource(
        noteNetworkDataSourceImpl: NoteNetworkDataSourceImpl
    ): NoteNetworkDataSource

    @Singleton
    @Binds
    abstract fun bindLabelNetworkDataSource(
        labelNetworkDataSourceImpl: LabelNetworkDataSourceImpl
    ): LabelNetworkDataSource
}
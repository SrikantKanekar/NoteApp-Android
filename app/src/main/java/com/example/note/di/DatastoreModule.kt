package com.example.note.di

import com.example.note.framework.datasource.datastore.AccountDatastore
import com.example.note.framework.datasource.datastore.SettingDataStore
import com.example.note.framework.presentation.ui.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Singleton
    @Provides
    fun provideAccountDatastore(
        application: BaseApplication
    ): AccountDatastore {
        return AccountDatastore(application)
    }

    @Singleton
    @Provides
    fun provideSettingDatastore(
        application: BaseApplication
    ): SettingDataStore {
        return SettingDataStore(application)
    }
}
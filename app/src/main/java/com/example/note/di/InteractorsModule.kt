package com.example.note.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

//    @Provides
//    fun provideConnectInteractors(
//        app: BaseApplication
//    ): ConnectInteractors {
//        return ConnectInteractors(
//            ConnectToFaircon(app),
//            DisconnectFromFaircon(app)
//        )
//    }
}
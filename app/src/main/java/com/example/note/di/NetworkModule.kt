package com.example.note.di

import com.example.note.business.data.network.DeletedNotesNetworkDataSource
import com.example.note.business.data.network.NoteNetworkDataSource
import com.example.note.business.domain.util.Urls.Companion.BASE_URL
import com.example.note.framework.datasource.datastore.AccountDatastore
import com.example.note.framework.datasource.network.api.AuthApi
import com.example.note.framework.datasource.network.api.DeletedNotesApi
import com.example.note.framework.datasource.network.api.NoteApi
import com.example.note.framework.datasource.network.interceptor.BasicAuthInterceptor
import com.example.note.framework.datasource.network.mapper.NoteDtoMapper
import com.example.note.framework.datasource.network.service.DeletedNotesNetworkService
import com.example.note.framework.datasource.network.service.NoteNetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBasicAuthInterceptor(
        accountDatastore: AccountDatastore
    ): BasicAuthInterceptor {
        return BasicAuthInterceptor(accountDatastore)
    }

    @Provides
    fun provideOkHttp(
        basicAuthInterceptor: BasicAuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(basicAuthInterceptor)
            .build()
    }

    @AuthRetrofit
    @Provides
    fun provideAuthRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @DefaultRetrofit
    @Provides
    fun provideDefaultRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideAuthApi(
        @DefaultRetrofit retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun provideNoteApi(
        @AuthRetrofit retrofit: Retrofit
    ): NoteApi {
        return retrofit.create(NoteApi::class.java)
    }

    @Provides
    fun provideDeletedNotesApi(
        @AuthRetrofit retrofit: Retrofit
    ): DeletedNotesApi {
        return retrofit.create(DeletedNotesApi::class.java)
    }

    @Provides
    fun provideNoteNetworkDataSource(
        noteApi: NoteApi,
        noteDtoMapper: NoteDtoMapper
    ): NoteNetworkDataSource {
        return NoteNetworkService(noteApi, noteDtoMapper)
    }

    @Provides
    fun provideDeletedNotesNetworkDataSource(
        deletedNotesApi: DeletedNotesApi,
        noteDtoMapper: NoteDtoMapper
    ): DeletedNotesNetworkDataSource {
        return DeletedNotesNetworkService(deletedNotesApi, noteDtoMapper)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultRetrofit
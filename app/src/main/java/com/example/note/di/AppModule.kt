package com.example.note.di

import android.content.Context
import com.example.note.business.domain.util.DateUtil
import com.example.note.framework.presentation.ui.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(
        @ApplicationContext application: Context
    ): BaseApplication {
        return application as BaseApplication
    }

    @Singleton
    @Provides
    fun provideDateFormat(): SimpleDateFormat {
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
        format.timeZone = TimeZone.getTimeZone("UTC-7")
        return format
    }

    @Singleton
    @Provides
    fun provideDateUtil(dateFormat: SimpleDateFormat): DateUtil {
        return DateUtil(dateFormat)
    }
}
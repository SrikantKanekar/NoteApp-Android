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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApplication(
        @ApplicationContext application: Context
    ): BaseApplication {
        return application as BaseApplication
    }

    @Provides
    fun provideDateFormat(): SimpleDateFormat {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC-7")
        return sdf
    }

    @Provides
    fun provideDateUtil(dateFormat: SimpleDateFormat): DateUtil {
        return DateUtil(dateFormat)
    }
}
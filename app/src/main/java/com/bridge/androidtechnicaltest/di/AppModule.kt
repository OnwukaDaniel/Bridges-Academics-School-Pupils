package com.bridge.androidtechnicaltest.di

import android.content.Context
import com.bridge.androidtechnicaltest.db.AppDatabase
import com.bridge.androidtechnicaltest.db.PupilDao
import com.bridge.androidtechnicaltest.network.PupilApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePupilApi(): PupilApi {
        return PupilAPIFactory.retrofitPupil()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return DatabaseFactory.getDBInstance(context)
    }

    @Provides
    fun providePupilDao(db: AppDatabase): PupilDao {
        return db.pupilDao()
    }
}

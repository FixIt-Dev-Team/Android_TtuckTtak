package com.gachon.ttuckttak.di

import android.content.Context
import androidx.room.Room
import com.gachon.ttuckttak.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "user_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDiagnosisDao(database: AppDatabase) = database.diagnosisDao()

}
package com.gachon.ttuckttak.di

import com.gachon.ttuckttak.repository.DiagnosisRepository
import com.gachon.ttuckttak.repository.DiagnosisRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDiagnosisRepository(repository: DiagnosisRepositoryImpl): DiagnosisRepository
}
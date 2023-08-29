package com.gachon.ttuckttak.di

import com.gachon.ttuckttak.repository.AuthRepository
import com.gachon.ttuckttak.repository.AuthRepositoryImpl
import com.gachon.ttuckttak.repository.DiagnosisRepository
import com.gachon.ttuckttak.repository.DiagnosisRepositoryImpl
import com.gachon.ttuckttak.repository.UserRepository
import com.gachon.ttuckttak.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindDiagnosisRepository(repository: DiagnosisRepositoryImpl): DiagnosisRepository

    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository
}
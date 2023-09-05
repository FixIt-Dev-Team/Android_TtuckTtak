package com.gachon.ttuckttak.di

import com.gachon.ttuckttak.repository.auth.AuthRepository
import com.gachon.ttuckttak.repository.auth.AuthRepositoryImpl
import com.gachon.ttuckttak.repository.auth.LocalAuthDataSource
import com.gachon.ttuckttak.repository.auth.LocalAuthDataSourceImpl
import com.gachon.ttuckttak.repository.auth.RemoteAuthDataSource
import com.gachon.ttuckttak.repository.auth.RemoteAuthDataSourceImpl
import com.gachon.ttuckttak.repository.user.LocalUserDataSource
import com.gachon.ttuckttak.repository.user.LocalUserDataSourceImpl
import com.gachon.ttuckttak.repository.user.RemoteUserDataSource
import com.gachon.ttuckttak.repository.user.RemoteUserDataSourceImpl
import com.gachon.ttuckttak.repository.user.UserRepository
import com.gachon.ttuckttak.repository.user.UserRepositoryImpl
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
    abstract fun bindLocalAuthDataSource(repository: LocalAuthDataSourceImpl): LocalAuthDataSource

    @Binds
    abstract fun bindRemoteAuthDataSource(repository: RemoteAuthDataSourceImpl): RemoteAuthDataSource

    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindLocalUserDataSource(repository: LocalUserDataSourceImpl): LocalUserDataSource

    @Binds
    abstract fun bindRemoteUserDataSource(repository: RemoteUserDataSourceImpl): RemoteUserDataSource
}
package com.example.mvisampleapp.di

import com.example.mvisampleapp.data.remote.ApiService
import com.example.mvisampleapp.data.repository.UserRepositoryImpl
import com.example.mvisampleapp.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository =
        UserRepositoryImpl(apiService)

}
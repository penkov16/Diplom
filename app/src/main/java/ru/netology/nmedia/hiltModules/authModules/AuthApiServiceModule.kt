package ru.netology.nmedia.hiltModules.authModules

import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.api.AuthApiService
import ru.netology.nmedia.api.okhttp
import ru.netology.nmedia.api.retrofit
import ru.netology.nmedia.api.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AuthApiServiceModule {
    @Provides
    @Singleton
    fun provideAuthApiService(): AuthApiService {
        return retrofit(okhttp())
            .create(AuthApiService::class.java)
    }
}
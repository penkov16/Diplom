package ru.netology.nmedia.hiltModules.userModules

import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.api.*
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UserApiServiceModule {
    @Provides
    @Singleton
    fun provideUserApiService(auth: AppAuth): UserApiService {
        return retrofit(okhttp(loggingInterceptor(), authInterceptor(auth)))
            .create(UserApiService::class.java)
    }
}
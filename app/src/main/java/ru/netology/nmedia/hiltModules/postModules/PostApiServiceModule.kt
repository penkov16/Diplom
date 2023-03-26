package ru.netology.nmedia.hiltModules.postModules

import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.api.*
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PostApiServiceModule {
    @Provides
    @Singleton
    fun providePostApiService(auth: AppAuth): PostApiService {
        return retrofit(okhttp(loggingInterceptor(), authInterceptor(auth)))
            .create(PostApiService::class.java)
    }
}
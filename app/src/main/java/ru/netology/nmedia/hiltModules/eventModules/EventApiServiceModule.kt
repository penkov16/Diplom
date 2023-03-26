package ru.netology.nmedia.hiltModules.eventModules

import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.api.*
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class EventApiServiceModule {
    @Provides
    @Singleton
    fun provideEventApiService(auth: AppAuth): EventApiService {
        return retrofit(okhttp(loggingInterceptor(), authInterceptor(auth)))
            .create(EventApiService::class.java)
    }
}
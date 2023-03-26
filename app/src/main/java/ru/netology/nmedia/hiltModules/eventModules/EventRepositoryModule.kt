package ru.netology.nmedia.hiltModules.eventModules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.repository.eventRepository.EventRepository
import ru.netology.nmedia.repository.eventRepository.EventRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPostRepository(imp: EventRepositoryImpl): EventRepository
}
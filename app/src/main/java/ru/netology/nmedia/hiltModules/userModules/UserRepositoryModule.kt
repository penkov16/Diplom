package ru.netology.nmedia.hiltModules.userModules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.repository.userRepository.UserRepository
import ru.netology.nmedia.repository.userRepository.UserRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(imp: UserRepositoryImpl): UserRepository
}
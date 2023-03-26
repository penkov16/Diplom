package ru.netology.nmedia.hiltModules.authModules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.repository.authRepository.AuthRepository
import ru.netology.nmedia.repository.authRepository.AuthRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(imp: AuthRepositoryImpl): AuthRepository
}
package ru.netology.nmedia.hiltModules.postModules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.repository.postRepository.PostRepository
import ru.netology.nmedia.repository.postRepository.PostRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PostRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPostRepository(imp: PostRepositoryImpl): PostRepository
}
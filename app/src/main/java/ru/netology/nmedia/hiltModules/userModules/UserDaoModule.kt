package ru.netology.nmedia.hiltModules.userModules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.dao.UserDao
import ru.netology.nmedia.db.AppDb

@InstallIn(SingletonComponent::class)
@Module
object UserDaoModule {
    @Provides
    fun provideUserDao(db: AppDb): UserDao = db.userDao()
}
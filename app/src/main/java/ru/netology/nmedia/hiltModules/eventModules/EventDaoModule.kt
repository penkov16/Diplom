package ru.netology.nmedia.hiltModules.eventModules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.dao.EventDao
import ru.netology.nmedia.dao.EventRemoteKeyDao
import ru.netology.nmedia.db.AppDb

@InstallIn(SingletonComponent::class)
@Module
object EventDaoModule {
    @Provides
    fun provideEventDao(db: AppDb): EventDao = db.eventDao()

    @Provides
    fun provideEventRemoteKeyDao(db: AppDb): EventRemoteKeyDao = db.eventRemoteKeyDao()
}
package ru.netology.nmedia.repository.postRepository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.entity.RemoteKeyType
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.PostRemoteKeyEntity
import ru.netology.nmedia.errors.ApiError
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val apiService: PostApiService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val db: AppDb
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PostEntity>): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> apiService.getLatest(state.config.initialLoadSize)
                LoadType.PREPEND -> {
                    val firstId = postRemoteKeyDao.max() ?: return MediatorResult.Success(false)
                    apiService.getAfter(firstId, state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val lastId = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    apiService.getBefore(lastId, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(
                response.code(),
                response.message(),
            )

            db.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        when (db.postRemoteKeyDao().isEmpty()) {
                            true -> postRemoteKeyDao.insert(
                                listOf(
                                    PostRemoteKeyEntity(RemoteKeyType.BEFORE, body.last().id),
                                    PostRemoteKeyEntity(RemoteKeyType.AFTER, body.first().id),
                                )
                            )
                            false -> postRemoteKeyDao.insert(
                                PostRemoteKeyEntity(RemoteKeyType.BEFORE, body.last().id),
                            )
                        }
                    }
                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(RemoteKeyType.BEFORE, body.last().id),
                        )
                    }
                    LoadType.PREPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(RemoteKeyType.AFTER, body.first().id),
                        )
                    }
                }
            }
            val list = body.map{ PostEntity.fromDto(it)}
            Log.i("Mediator posts", body.toString())
            Log.i("Mediator entities", list.toString())
            postDao.insert(body.map{ PostEntity.fromDto(it)})

            return MediatorResult.Success(body.isEmpty())
        } catch (e: Exception) {
            Log.e("Mediator error", e.message.toString())
            return MediatorResult.Error(e)
        }
    }
}
package ru.netology.nmedia.repository.postRepository

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.AttachmentType
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.errors.*
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val PAGE_SIZE = 5
const val ENABLE_PLACE_HOLDERS = false

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val dao: PostDao,
    private val apiService: PostApiService,
    mediator: PostRemoteMediator
) : PostRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = ENABLE_PLACE_HOLDERS),
        remoteMediator = mediator,
        pagingSourceFactory = { dao.getAll() },
    ).flow.map { pagingData ->
        pagingData.map(PostEntity::toDto)
    }

    override suspend fun getPostById(id: Long) = dao.getPostById(id)?.toDto() ?: throw DbError

    override suspend fun getMaxId() = dao.getPostMaxId()?.toDto()?.id ?: throw DbError

    override suspend fun likeById(id: Long) {
        try {
            val response = apiService.likeById(id)
            checkResponse(response)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: ApiError) {
            throw e
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun dislikeById(id: Long) {
        try {
            val response = apiService.dislikeById(id)
            checkResponse(response)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: ApiError) {
            throw e
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun save(post: Post, retry: Boolean) {
        try {
            val response = apiService.save(post)
            checkResponse(response)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: ApiError) {
            throw e
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun uploadMedia(upload: MediaUpload): Attachment {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )
            val response = apiService.upload(media)
            checkResponse(response)
            val mediaResponse = response.body() ?: throw ApiError(response.code(), response.message())
            return Attachment(mediaResponse.url, AttachmentType.IMAGE)
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload, retry: Boolean) {
        try {
            val attachment = uploadMedia(upload)
            val postWithAttachment = post.copy(attachment = attachment)
            save(postWithAttachment, retry)
        } catch (e: AppError) {
            throw e
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun removeById(id: Long) {
        try {
            dao.removeById(id)
            val response = apiService.removeById(id)
            checkResponse(response)
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}

fun checkResponse(response: Response<out Any>) {
    if (!response.isSuccessful) {
        throw ApiError(response.code(), response.message())
    }
}
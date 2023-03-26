package ru.netology.nmedia.repository.userRepository

import androidx.lifecycle.asLiveData
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response
import ru.netology.nmedia.api.UserApiService
import ru.netology.nmedia.dao.UserDao
import ru.netology.nmedia.dto.User
import ru.netology.nmedia.entity.UserEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.errors.ApiError
import ru.netology.nmedia.errors.NetworkException
import ru.netology.nmedia.errors.UnknownException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val apiService: UserApiService
) : UserRepository {

    override val data = dao.getAll()
        .map(List<UserEntity>::toDto)
        .flowOn(Dispatchers.Default)
        .asLiveData()

    override suspend fun getById(id: Long): User {
        try {
            val response = apiService.getById(id)
            checkResponse(response)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(UserEntity.fromDto(body))
            return body
        } catch (e: ApiException) {
            throw e
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun getAll() {
        try {
            val response = apiService.getAll()
            checkResponse(response)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: ApiException) {
            throw e
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}

private fun checkResponse(response: Response<out Any>) {
    if (!response.isSuccessful) {
        throw ApiError(response.code(), response.message())
    }
}
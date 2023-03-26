package ru.netology.nmedia.repository.authRepository

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import ru.netology.nmedia.api.AuthApiService
import ru.netology.nmedia.dto.AuthState
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.errors.ApiError2
import ru.netology.nmedia.errors.NetworkException
import ru.netology.nmedia.repository.authRepository.AuthRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService
) : AuthRepository {
    override suspend fun updateUser(login: String, pass: String): AuthState {
        try {
            val response = apiService.updateUser(login, pass)
            checkResponse(response)
            return response.body() ?: throw ApiError2(response.code(), response.message(), response.errorBody())
        } catch (e: IOException) {
            throw NetworkException
        }
    }

    override suspend fun registerUser(login: String, pass: String, name: String): AuthState {
        try {
            val response = apiService.registerUser(login, pass, name)
            checkResponse(response)
            return response.body() ?: throw ApiError2(response.code(), response.message(), response.errorBody())
        } catch (e: IOException) {
            throw NetworkException
        }
    }

    override suspend fun registerWithPhoto(
        login: String,
        password: String,
        name: String,
        upload: MediaUpload
    ): AuthState {
        val media = MultipartBody.Part.createFormData(
            "file", upload.file.name, upload.file.asRequestBody()
        )

        val response = apiService.registerWithPhoto(
            login.toRequestBody(),
            password.toRequestBody(),
            name.toRequestBody(),
            media
        )

        checkResponse(response)
        return response.body() ?: throw ApiError2(response.code(), response.message(), response.errorBody())
    }

    private fun checkResponse(response: Response<out Any>) {
        if (!response.isSuccessful) {
            throw ApiError2(response.code(), response.message(), response.errorBody())
        }
    }
}
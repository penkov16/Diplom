package ru.netology.nmedia.repository.authRepository

import ru.netology.nmedia.dto.AuthState
import ru.netology.nmedia.dto.MediaUpload

interface AuthRepository {
    suspend fun updateUser(login: String, pass: String): AuthState
    suspend fun registerUser(login: String, pass: String, name: String): AuthState
    suspend fun registerWithPhoto(login: String, pass: String, name: String, upload: MediaUpload): AuthState
}
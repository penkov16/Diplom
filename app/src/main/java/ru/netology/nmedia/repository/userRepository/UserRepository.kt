package ru.netology.nmedia.repository.userRepository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.User

interface UserRepository {
    val data: LiveData<List<User>>
    suspend fun getById(id: Long): User
    suspend fun getAll()
}
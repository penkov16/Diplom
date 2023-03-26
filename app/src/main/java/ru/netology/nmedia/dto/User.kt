package ru.netology.nmedia.dto

data class User(
    val id: Long = 0,
    val login: String = "",
    val name: String = "",
    val avatar: String? = null
)

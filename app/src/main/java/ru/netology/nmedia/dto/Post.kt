package ru.netology.nmedia.dto

import com.google.gson.annotations.SerializedName
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Coords

data class Post(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("authorId")
    val authorId: Long = 0,
    @SerializedName("author")
    val author: String = "",
    @SerializedName("authorAvatar")
    val authorAvatar: String? = "",
    @SerializedName("content")
    val content: String = "",
    @SerializedName("published")
    val published: String = "",
    @SerializedName("coords")
    val coords: Coords? = null,
    @SerializedName("link")
    val link: String? = "",
    @SerializedName("mentionIds")
    val mentionIds: List<Long> = emptyList(),
    @SerializedName("mentionedMe")
    val mentionedMe: Boolean = false,
    @SerializedName("likeOwnerIds")
    val likeOwnerIds: List<Long> = emptyList(),
    @SerializedName("likedByMe")
    val likedByMe: Boolean = false,
    @SerializedName("attachment")
    val attachment: Attachment? = null,
)
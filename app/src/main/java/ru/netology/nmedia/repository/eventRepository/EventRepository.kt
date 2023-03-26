package ru.netology.nmedia.repository.eventRepository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.MediaUpload

interface EventRepository {
    val data: Flow<PagingData<Event>>
    suspend fun likeById(id: Long)
    suspend fun dislikeById(id: Long)
    suspend fun save(event: Event, retry: Boolean)
    suspend fun removeById(id: Long)
    suspend fun saveWithAttachment(event: Event, upload: MediaUpload, retry: Boolean)
    suspend fun getById(id: Long): Event
    suspend fun getMaxId(): Long
    suspend fun uploadMedia(upload: MediaUpload): Attachment
    suspend fun joinById(id: Long)
    suspend fun rejectById(id: Long)

}
package ru.netology.nmedia.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.*


interface EventApiService {
    @GET("events")
    suspend fun getAll(): Response<List<Post>>

    @GET("events/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Event>

    @POST("events/")
    suspend fun save(@Body event: Event): Response<Event>

    @DELETE("events/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("events/{id}/likes/")
    suspend fun likeById(@Path("id") id: Long): Response<Event>

    @DELETE("events/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<Event>

    @GET("events/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Event>>

    @GET("events/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Event>>

    @GET("events/{id}/before")
    suspend fun getBefore(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Event>>

    @GET("events/{id}/after")
    suspend fun getAfter(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Event>>

    @Multipart
    @POST("media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>

    @POST("events/{id}/participants")
    suspend fun participate(@Path("id") id: Long): Response<Event>

    @DELETE("events/{id}/participants")
    suspend fun rejection(@Path("id") id: Long): Response<Event>

}
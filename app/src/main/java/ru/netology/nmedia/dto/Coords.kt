package ru.netology.nmedia.dto

import com.google.gson.annotations.SerializedName

data class Coords(
    @SerializedName("lat")
    val lat: Float,
    @SerializedName("long")
    val lon: Float
)
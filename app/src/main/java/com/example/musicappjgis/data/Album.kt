package com.example.musicappjgis.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Album(
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("id") val id: String
) : Serializable

package com.example.musicappjgis.data

import retrofit2.http.GET
import retrofit2.http.Path

data class AlbumDetailResponse(
    val id: String,
    val title: String,
    val artist: String,
    val image: String,
    val description: String
)

interface AlbumApi {
    @GET("albums/{id}")
    suspend fun getAlbumDetail(@Path("id") id: String): AlbumDetailResponse
}

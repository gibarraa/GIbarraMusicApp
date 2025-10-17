package com.example.musicappjgis.data

object AlbumRepository {
    suspend fun getAlbums(): Result<List<Album>> {
        return try {
            val data = ApiClient.musicApi.getAlbums()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbum(id: String): Result<Album> {
        return try {
            val data = ApiClient.musicApi.getAlbum(id)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

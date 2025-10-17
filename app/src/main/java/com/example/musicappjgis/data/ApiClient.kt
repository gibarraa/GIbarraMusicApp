package com.example.musicappjgis.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://music.juanfrausto.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val musicApi: MusicApi by lazy {
        retrofit.create(MusicApi::class.java)
    }
}

package com.example.vinilostsdc_frontend.di

import com.example.vinilostsdc_frontend.data.repository.AlbumRepository
import com.example.vinilostsdc_frontend.data.repository.AlbumRepositoryImpl
import com.example.vinilostsdc_frontend.data.repository.ArtistRepository
import com.example.vinilostsdc_frontend.data.repository.ArtistRepositoryImpl
import com.example.vinilostsdc_frontend.data.repository.CollectorRepository
import com.example.vinilostsdc_frontend.data.repository.CollectorRepositoryImpl
import com.example.vinilostsdc_frontend.data.service.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private const val BASE_URL = "https://back-vynils-qa.herokuapp.com/"

    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

object RepositoryModule {

    val albumRepository: AlbumRepository by lazy {
        AlbumRepositoryImpl(NetworkModule.apiService)
    }

    val artistRepository: ArtistRepository by lazy {
        ArtistRepositoryImpl(NetworkModule.apiService)
    }

    val collectorRepository: CollectorRepository by lazy {
        CollectorRepositoryImpl(NetworkModule.apiService)
    }
}
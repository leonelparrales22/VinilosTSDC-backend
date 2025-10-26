package com.example.vinilostsdc_frontend.data.service

import com.example.vinilostsdc_frontend.data.model.Album
import com.example.vinilostsdc_frontend.data.model.Artist
import com.example.vinilostsdc_frontend.data.model.Collector
import com.example.vinilostsdc_frontend.data.model.CreateAlbumRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Albums endpoints
    @GET("albums")
    suspend fun getAlbums(): Response<List<Album>>
    
    @GET("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: Int): Response<Album>
    
    @POST("albums")
    suspend fun createAlbum(@Body album: CreateAlbumRequest): Response<Album>
    
    // Artists endpoints
    @GET("musicians")
    suspend fun getArtists(): Response<List<Artist>>
    
    @GET("musicians/{id}")
    suspend fun getArtistById(@Path("id") id: Int): Response<Artist>
    
    // Collectors endpoints
    @GET("collectors")
    suspend fun getCollectors(): Response<List<Collector>>
    
    @GET("collectors/{id}")
    suspend fun getCollectorById(@Path("id") id: Int): Response<Collector>
}
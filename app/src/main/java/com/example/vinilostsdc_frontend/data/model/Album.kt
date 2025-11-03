package com.example.vinilostsdc_frontend.data.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("cover")
    val cover: String,
    
    @SerializedName("releaseDate")
    val releaseDate: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("genre")
    val genre: String,
    
    @SerializedName("recordLabel")
    val recordLabel: String? = null,
    
    @SerializedName("tracks")
    val tracks: List<Track> = emptyList()
)

data class Track(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("duration")
    val duration: String
)

data class CreateAlbumRequest(
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String? = null
)
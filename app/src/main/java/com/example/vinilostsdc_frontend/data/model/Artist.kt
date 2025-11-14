package com.example.vinilostsdc_frontend.data.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Artist(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("image")
    val image: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("birthDate")
    val birthDate: String? = null
    ,
    
    @SerializedName("albums")
    val albums: List<Album>? = null
)
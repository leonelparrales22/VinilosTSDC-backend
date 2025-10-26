package com.example.vinilostsdc_frontend.data.model

import com.google.gson.annotations.SerializedName

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
)
package com.example.vinilostsdc_frontend.data.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Collector(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("telephone")
    val telephone: String,
    
    @SerializedName("email")
    val email: String
)
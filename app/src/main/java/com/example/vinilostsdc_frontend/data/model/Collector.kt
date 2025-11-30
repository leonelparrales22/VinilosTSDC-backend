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
    val email: String,

    @SerializedName("comments")
    val comments: List<CollectorComment> = emptyList(),

    @SerializedName("favoritePerformers")
    val favoritePerformers: List<FavoritePerformer> = emptyList(),

    @SerializedName("collectorAlbums")
    val collectorAlbums: List<CollectorAlbum> = emptyList(),

    // Imagen: se toma la del primer favorito si existe, si no null
    val image: String? = favoritePerformers.firstOrNull()?.image
)

@Immutable
data class CollectorComment(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("description") val description: String = "",
    @SerializedName("rating") val rating: Int = 0
)

@Immutable
data class FavoritePerformer(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("birthDate") val birthDate: String? = null
)

@Immutable
data class CollectorAlbum(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("price") val price: Int = 0,
    @SerializedName("status") val status: String = ""
)
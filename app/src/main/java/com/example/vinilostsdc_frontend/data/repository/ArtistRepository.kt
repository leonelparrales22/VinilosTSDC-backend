package com.example.vinilostsdc_frontend.data.repository

import com.example.vinilostsdc_frontend.data.model.Artist
import com.example.vinilostsdc_frontend.data.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

interface ArtistRepository {
    fun getArtists(): Flow<Resource<List<Artist>>>
    fun getArtistById(id: Int): Flow<Resource<Artist>>
}

class ArtistRepositoryImpl(
    private val apiService: ApiService
) : ArtistRepository {

    override fun getArtists(): Flow<Resource<List<Artist>>> = flow {
        try {
            emit(Resource.Loading())
            val response = withContext(Dispatchers.IO) { apiService.getArtists() }
            if (response.isSuccessful) {
                val artists = response.body() ?: emptyList()
                emit(Resource.Success(artists))
            } else {
                emit(Resource.Error("Error al obtener los artistas: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        }
    }

    override fun getArtistById(id: Int): Flow<Resource<Artist>> = flow {
        try {
            emit(Resource.Loading())
            val response = withContext(Dispatchers.IO) { apiService.getArtistById(id) }
            if (response.isSuccessful) {
                val artist = response.body()
                if (artist != null) {
                    emit(Resource.Success(artist))
                } else {
                    emit(Resource.Error("Artista no encontrado"))
                }
            } else {
                emit(Resource.Error("Error al obtener el artista: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        }
    }
}
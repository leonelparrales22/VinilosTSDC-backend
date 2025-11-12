package com.example.vinilostsdc_frontend.data.repository

import com.example.vinilostsdc_frontend.data.model.Album
import com.example.vinilostsdc_frontend.data.model.CreateAlbumRequest
import com.example.vinilostsdc_frontend.data.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    data class Loading<T>(val data: T? = null) : Resource<T>()
}

interface AlbumRepository {
    fun getAlbums(): Flow<Resource<List<Album>>>
    fun getAlbumById(id: Int): Flow<Resource<Album>>
    fun createAlbum(albumRequest: CreateAlbumRequest): Flow<Resource<Album>>
}

class AlbumRepositoryImpl(
    private val apiService: ApiService
) : AlbumRepository {

    override fun getAlbums(): Flow<Resource<List<Album>>> = flow {
        try {
            emit(Resource.Loading())
            val response = withContext(Dispatchers.IO) { apiService.getAlbums() }
            if (response.isSuccessful) {
                val albums = response.body() ?: emptyList()
                emit(Resource.Success(albums))
            } else {
                emit(Resource.Error("Error al obtener los álbumes: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        }
    }

    override fun getAlbumById(id: Int): Flow<Resource<Album>> = flow {
        try {
            emit(Resource.Loading())
            val response = withContext(Dispatchers.IO) { apiService.getAlbumById(id) }
            if (response.isSuccessful) {
                val album = response.body()
                if (album != null) {
                    emit(Resource.Success(album))
                } else {
                    emit(Resource.Error("Álbum no encontrado"))
                }
            } else {
                emit(Resource.Error("Error al obtener el álbum: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        }
    }

    override fun createAlbum(albumRequest: CreateAlbumRequest): Flow<Resource<Album>> = flow {
        try {
            emit(Resource.Loading())
            val response = withContext(Dispatchers.IO) { apiService.createAlbum(albumRequest) }
            if (response.isSuccessful) {
                val album = response.body()
                if (album != null) {
                    emit(Resource.Success(album))
                } else {
                    emit(Resource.Error("Error al crear el álbum"))
                }
            } else {
                emit(Resource.Error("Error al crear el álbum: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        }
    }
}
package com.example.vinilostsdc_frontend.data.repository

import com.example.vinilostsdc_frontend.data.model.Collector
import com.example.vinilostsdc_frontend.data.service.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CollectorRepository {
    fun getCollectors(): Flow<Resource<List<Collector>>>
    fun getCollectorById(id: Int): Flow<Resource<Collector>>
}

class CollectorRepositoryImpl(
    private val apiService: ApiService
) : CollectorRepository {

    override fun getCollectors(): Flow<Resource<List<Collector>>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getCollectors()
            if (response.isSuccessful) {
                val collectors = response.body() ?: emptyList()
                emit(Resource.Success(collectors))
            } else {
                emit(Resource.Error("Error al obtener los coleccionistas: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        }
    }

    override fun getCollectorById(id: Int): Flow<Resource<Collector>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getCollectorById(id)
            if (response.isSuccessful) {
                val collector = response.body()
                if (collector != null) {
                    emit(Resource.Success(collector))
                } else {
                    emit(Resource.Error("Coleccionista no encontrado"))
                }
            } else {
                emit(Resource.Error("Error al obtener el coleccionista: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        }
    }
}
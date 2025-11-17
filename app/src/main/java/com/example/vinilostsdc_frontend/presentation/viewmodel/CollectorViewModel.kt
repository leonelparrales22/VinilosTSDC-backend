package com.example.vinilostsdc_frontend.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinilostsdc_frontend.data.model.Collector
import com.example.vinilostsdc_frontend.data.repository.CollectorRepository
import com.example.vinilostsdc_frontend.data.repository.Resource
import com.example.vinilostsdc_frontend.di.RepositoryModule
import com.example.vinilostsdc_frontend.utils.ProfilingUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CollectorUiState(
    val collectors: List<Collector> = emptyList(),
    val selectedCollector: Collector? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CollectorViewModel(
    private val collectorRepository: CollectorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectorUiState())
    val uiState: StateFlow<CollectorUiState> = _uiState.asStateFlow()

    fun getCollectors(context: Context? = null) {
        viewModelScope.launch {
            if (context != null) {
                ProfilingUtils.profileOperation(context, "HU05 - Consultar listado de coleccionistas") {
                    collectorRepository.getCollectors().collect { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = true,
                                    errorMessage = null
                                )
                            }
                            is Resource.Success -> {
                                _uiState.value = _uiState.value.copy(
                                    collectors = resource.data,
                                    isLoading = false,
                                    errorMessage = null
                                )
                            }
                            is Resource.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    errorMessage = resource.message
                                )
                            }
                        }
                    }
                }
            } else {
                collectorRepository.getCollectors().collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                collectors = resource.data,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun getCollectorById(id: Int) {
        viewModelScope.launch {
            collectorRepository.getCollectorById(id).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            selectedCollector = resource.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

class CollectorViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollectorViewModel(RepositoryModule.collectorRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
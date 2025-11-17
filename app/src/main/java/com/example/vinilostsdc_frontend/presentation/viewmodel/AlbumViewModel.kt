package com.example.vinilostsdc_frontend.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinilostsdc_frontend.data.model.Album
import com.example.vinilostsdc_frontend.data.model.CreateAlbumRequest
import com.example.vinilostsdc_frontend.data.repository.AlbumRepository
import com.example.vinilostsdc_frontend.data.repository.Resource
import com.example.vinilostsdc_frontend.di.RepositoryModule
import com.example.vinilostsdc_frontend.utils.ProfilingUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AlbumUiState(
    val albums: List<Album> = emptyList(),
    val selectedAlbum: Album? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreatingAlbum: Boolean = false,
    val albumCreated: Boolean = false
)

class AlbumViewModel(
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState: StateFlow<AlbumUiState> = _uiState.asStateFlow()

    fun getAlbums(context: Context? = null) {
        viewModelScope.launch {
            if (context != null) {
                ProfilingUtils.profileOperation(context, "HU01 - Consultar catálogo de álbumes") {
                    albumRepository.getAlbums().collect { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = true,
                                    errorMessage = null
                                )
                            }
                            is Resource.Success -> {
                                _uiState.value = _uiState.value.copy(
                                    albums = resource.data,
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
                albumRepository.getAlbums().collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                albums = resource.data,
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

    fun getAlbumById(context: Context, id: Int) {
        viewModelScope.launch {
            ProfilingUtils.profileOperation(context, "HU02 - Consultar la información detallada de un álbum") {
                albumRepository.getAlbumById(id).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                selectedAlbum = resource.data,
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

    fun createAlbum(
        name: String,
        cover: String,
        releaseDate: String,
        description: String,
        genre: String,
        recordLabel: String? = null
    ) {
        viewModelScope.launch {
            val albumRequest = CreateAlbumRequest(
                name = name,
                cover = cover,
                releaseDate = releaseDate,
                description = description,
                genre = genre,
                recordLabel = recordLabel
            )

            albumRepository.createAlbum(albumRequest).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isCreatingAlbum = true,
                            errorMessage = null,
                            albumCreated = false
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isCreatingAlbum = false,
                            errorMessage = null,
                            albumCreated = true
                        )
                        // Refresh the albums list
                        getAlbums()
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isCreatingAlbum = false,
                            errorMessage = resource.message,
                            albumCreated = false
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearAlbumCreated() {
        _uiState.value = _uiState.value.copy(albumCreated = false)
    }
}

class AlbumViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumViewModel(RepositoryModule.albumRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
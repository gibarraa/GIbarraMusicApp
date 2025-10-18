package com.example.musicappjgis.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicappjgis.data.Album
import com.example.musicappjgis.data.AlbumRepository
import com.example.musicappjgis.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _state = MutableStateFlow(UiState<Album>())
    val state: StateFlow<UiState<Album>> = _state

    fun loadAlbum(albumId: String) {
        _state.value = UiState(loading = true)
        viewModelScope.launch {
            val result = AlbumRepository.getAlbum(albumId)
            _state.value = result.fold(
                onSuccess = { UiState(loading = false, data = it) },
                onFailure = { UiState(loading = false, error = it.message ?: "Error al cargar álbum") }
            )
        }
    }
}

package com.tvlibre.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val loading: Boolean = true,
    val channels: List<Channel> = emptyList(),
    val current: Channel? = null,
    val error: String? = null,
)

class ChannelsViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ChannelRepository(app)
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            val list = repo.load()
            _state.update {
                it.copy(
                    loading = false,
                    channels = list,
                    error = if (list.isEmpty()) "No se encontraron canales" else null,
                )
            }
        }
    }

    fun select(channel: Channel) = _state.update { it.copy(current = channel) }
    fun close() = _state.update { it.copy(current = null) }
}

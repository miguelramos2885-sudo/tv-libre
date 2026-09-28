package com.tvlibre.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.darkColorScheme
import com.tvlibre.app.ChannelsViewModel

@Composable
fun TvLibreApp(vm: ChannelsViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val current = state.current
            if (current != null) {
                PlayerScreen(state.channels, current, onZap = vm::select, onExit = vm::close)
            } else {
                ChannelListScreen(state, onSelect = vm::select, onRetry = vm::load)
            }
        }
    }
}

package com.tvlibre.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Card
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.tvlibre.app.Channel
import com.tvlibre.app.UiState

@Composable
fun ChannelListScreen(state: UiState, onSelect: (Channel) -> Unit, onRetry: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(48.dp)) {
        Text("TV Libre", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))
        when {
            state.loading -> Text("Cargando canales…")
            state.error != null -> Column {
                Text(state.error)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onRetry) { Text("Reintentar") }
            }
            else -> LazyVerticalGrid(
                columns = GridCells.Adaptive(200.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(state.channels) { ch -> ChannelCard(ch, onSelect) }
            }
        }
    }
}

@Composable
private fun ChannelCard(ch: Channel, onSelect: (Channel) -> Unit) {
    Card(onClick = { onSelect(ch) }, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = ch.logo,
                contentDescription = null,
                modifier = Modifier.height(72.dp).fillMaxWidth(),
                contentScale = ContentScale.Fit,
            )
            Spacer(Modifier.height(8.dp))
            Text(ch.name, maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
        }
    }
}

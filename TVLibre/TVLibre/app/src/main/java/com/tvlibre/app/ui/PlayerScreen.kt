package com.tvlibre.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.Text
import com.tvlibre.app.Channel
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    channels: List<Channel>,
    current: Channel,
    onZap: (Channel) -> Unit,
    onExit: () -> Unit,
) {
    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build().apply { playWhenReady = true } }
    val focus = remember { FocusRequester() }
    var showInfo by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    BackHandler(onBack = onExit)

    // Carga el canal y oculta el nombre a los 3 segundos.
    LaunchedEffect(current) {
        error = null
        showInfo = true
        player.setMediaItem(MediaItem.fromUri(current.url))
        player.prepare()
        delay(3000)
        showInfo = false
    }

    // Reconexión automática a los 5 segundos si se corta el stream.
    LaunchedEffect(error) {
        if (error != null) {
            delay(5000)
            error = null
            player.prepare()
        }
    }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onPlayerError(e: PlaybackException) {
                error = "Sin señal. Reintentando…"
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }

    LaunchedEffect(Unit) { focus.requestFocus() }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focus)
            .focusable()
            .onKeyEvent { ev ->
                if (ev.type != KeyEventType.KeyDown || channels.isEmpty()) return@onKeyEvent false
                val i = channels.indexOf(current).coerceAtLeast(0)
                when (ev.key) {
                    Key.DirectionUp, Key.ChannelUp -> { onZap(channels[(i + 1) % channels.size]); true }
                    Key.DirectionDown, Key.ChannelDown -> {
                        onZap(channels[(i - 1 + channels.size) % channels.size]); true
                    }
                    Key.DirectionCenter, Key.Enter -> { showInfo = true; true }
                    else -> false
                }
            },
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = false
                    isFocusable = false
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
        if (showInfo) {
            Text(current.name, modifier = Modifier.align(Alignment.BottomStart).padding(48.dp))
        }
        error?.let { Text(it, modifier = Modifier.align(Alignment.Center)) }
    }
}

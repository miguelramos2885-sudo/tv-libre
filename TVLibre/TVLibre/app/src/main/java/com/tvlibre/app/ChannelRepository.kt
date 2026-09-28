package com.tvlibre.app

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class ChannelRepository(private val context: Context) {

    /** Descarga la lista remota; si falla o viene vacía, usa assets/channels.m3u. */
    suspend fun load(): List<Channel> = withContext(Dispatchers.IO) {
        val remote = runCatching { M3uParser.parse(fetch(PLAYLIST_URL)) }.getOrDefault(emptyList())
        remote.ifEmpty {
            val local = context.assets.open("channels.m3u").bufferedReader().use { it.readText() }
            M3uParser.parse(local)
        }
    }

    private fun fetch(url: String): String {
        val c = URL(url).openConnection() as HttpURLConnection
        c.connectTimeout = 8000
        c.readTimeout = 8000
        return c.inputStream.bufferedReader().use { it.readText() }
    }

    companion object {
        // Cambiá esta URL por tu propia lista. Usá solo canales de emisión libre.
        const val PLAYLIST_URL = "https://iptv-org.github.io/iptv/countries/ar.m3u"
    }
}

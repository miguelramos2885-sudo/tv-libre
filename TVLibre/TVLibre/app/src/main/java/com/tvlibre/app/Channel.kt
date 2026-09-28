package com.tvlibre.app

data class Channel(
    val name: String,
    val url: String,
    val logo: String? = null,
    val group: String? = null,
)

object M3uParser {
    private val attr = Regex("""([\w-]+)="([^"]*)"""")

    fun parse(text: String): List<Channel> {
        val out = mutableListOf<Channel>()
        var name: String? = null
        var logo: String? = null
        var group: String? = null
        for (raw in text.lineSequence()) {
            val line = raw.trim()
            if (line.startsWith("#EXTINF")) {
                val attrs = attr.findAll(line).associate { it.groupValues[1] to it.groupValues[2] }
                logo = attrs["tvg-logo"]?.ifBlank { null }
                group = attrs["group-title"]?.ifBlank { null }
                name = line.substringAfterLast(",").trim()
            } else if (line.isNotEmpty() && !line.startsWith("#")) {
                val n = name
                if (n != null && line.startsWith("http")) out += Channel(n, line, logo, group)
                name = null; logo = null; group = null
            }
        }
        return out
    }
}

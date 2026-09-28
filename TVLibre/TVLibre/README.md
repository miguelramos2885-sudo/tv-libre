# TV Libre (Android TV)

Reproductor de canales en abierto (HLS) con Kotlin, Compose for TV y Media3.

## Cómo abrirlo
1. Android Studio (Koala o más nuevo) > File > Open > carpeta `TVLibre`.
2. Esperar el Gradle Sync.
3. Crear un emulador "Android TV (1080p)" y ejecutar `app`.

## Canales
- `ChannelRepository.PLAYLIST_URL` apunta a una lista M3U remota. Cambiala por la tuya.
- Si no hay red, se usa `app/src/main/assets/channels.m3u`.
- Usá solo canales de emisión libre y verificá que las URLs sigan activas.

## Control remoto
- Grilla: D-pad + OK para abrir un canal.
- Reproductor: arriba/abajo (o CH+/CH-) cambia de canal, OK muestra el nombre, Atrás vuelve a la grilla.

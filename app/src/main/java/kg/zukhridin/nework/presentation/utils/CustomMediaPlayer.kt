package kg.zukhridin.nework.presentation.utils

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class CustomMediaPlayer(
    context: Context,
    private val playerView: PlayerView
) {
    private val exoPlayer = ExoPlayer.Builder(context).build()

    init {
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
    }

    fun stop() {
        exoPlayer.stop()
    }

    fun download(url: String) {
        val uri = Uri.parse(url)
        playerView.player = exoPlayer
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setController(use: Boolean = true) {
        playerView.useController = use
    }


    fun play() {
        exoPlayer.playWhenReady = true
        exoPlayer.play()

    }
}
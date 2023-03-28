package kg.zukhridin.nework.custom

import android.content.Context
import android.net.Uri
import android.widget.SeekBar
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kg.zukhridin.nework.dto.AttachmentType

class CustomMediaPlayer(
    context: Context,
    private val playerView: PlayerView,
    private val type: AttachmentType
) {
    private val exoPlayer = ExoPlayer.Builder(context).build()
    var isMute = false

    init {
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
    }
    fun stop(){
        exoPlayer.stop()
    }

    fun download(url: String) {
        val uri = Uri.parse(url)
        playerView.player = exoPlayer
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.addMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setPauseAndPlayOnClickListener() {
        playerView.player = exoPlayer
        if (exoPlayer.isPlaying) {
            pause()
        } else if (!exoPlayer.isPlaying) {
            play()
        }
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun setVolumeMute() {
        if (exoPlayer.volume > 0F) {
            exoPlayer.volume = 0F
            isMute = true

        } else {
            exoPlayer.volume = 1F
            isMute = false
        }
    }

    fun setController(use: Boolean) {
        playerView.useController = use
    }


    fun play() {
        exoPlayer.play()
    }
}
package kg.zukhridin.nework.adapter

import android.content.Context
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kg.zukhridin.nework.R
import kg.zukhridin.nework.custom.CustomMediaPlayer
import kg.zukhridin.nework.databinding.PostItemWithVideoBinding
import kg.zukhridin.nework.dto.Post

class PostViewHolderWithVideo(
    private val binding: PostItemWithVideoBinding,
    private val listener: PostOnItemEventsListener,
    private val activity: FragmentActivity,
    private val player: CustomMediaPlayer
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        if (player.isMute) {
            binding.volumeController.icon =
                ContextCompat.getDrawable(activity.applicationContext, R.drawable.baseline_volume_off)
        } else {
            binding.volumeController.icon =
                ContextCompat.getDrawable(activity.applicationContext, R.drawable.baseline_volume_up)
        }
        binding.author.text = post.author
        binding.coordination.text = post.attachment?.type.toString()
        binding.contentText.text = postContent(post.content, post.author, activity.assets)
        downloadVideo(
            binding.root.context,
            post.attachment?.url.toString(),
            player,
            binding.contentVideo,
            binding.volumeController,
            binding.playControl
        )
        binding.likeCount.text =
            post.likeOwnerIds.size.toString()
        binding.itemSettings.isVisible = post.ownedByMe
        binding.likeBtn.isChecked = post.likedByMe
        binding.likeBtn.setOnClickListener {

            listener.onLike(post)
        }
        downloadImage(
            binding.authorAvatar,
            post.authorAvatar
                ?: "https://aurora-piter.com/wp-content/uploads/2022/10/403019_avatar_male_man_person_user_icon.png"
        )
        if (!post.link.isNullOrBlank()) {
            binding.link.visibility = View.VISIBLE
            binding.link.text = post.link
        } else {
            binding.link.visibility = View.GONE
        }
        binding.published.text = post.published
    }
}

fun downloadVideo(
    context: Context,
    url: String,
    player: CustomMediaPlayer,
    playerView: PlayerView,
    volumeController: MaterialButton?,
    playControl: MaterialButton?,
) {
    playerView.doOnNextLayout {
        player.apply {
            stop()
            download(url)
            playerView.setOnClickListener {
                playControl?.visibility = View.VISIBLE
                playControl?.setOnClickListener {
                    if (playerView.player?.isPlaying == true) {
                        playControl.icon = ContextCompat.getDrawable(
                            context,
                            R.drawable.baseline_play_circle
                        )
                    } else {
                        playControl.icon = ContextCompat.getDrawable(
                            context,
                            R.drawable.baseline_pause_circle
                        )
                    }
                    setPauseAndPlayOnClickListener()
                }
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    playControl?.visibility = View.GONE
                }, 5000)
            }
            volumeController?.setOnClickListener {
                setVolumeMute()
                if (isMute) {
                    volumeController.icon =
                        ContextCompat.getDrawable(context, R.drawable.baseline_volume_off)
                } else {
                    volumeController.icon =
                        ContextCompat.getDrawable(context, R.drawable.baseline_volume_up)
                }
            }
            setController(false)
        }
    }
}
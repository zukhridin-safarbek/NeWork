package kg.zukhridin.nework.adapter.viewholders

import android.content.Context
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kg.zukhridin.nework.R
import kg.zukhridin.nework.adapter.PostOnItemEventsListener
import kg.zukhridin.nework.adapter.downloadAvatar
import kg.zukhridin.nework.adapter.downloadImage
import kg.zukhridin.nework.adapter.postContent
import kg.zukhridin.nework.custom.CoordinationControl
import kg.zukhridin.nework.custom.CustomMediaPlayer
import kg.zukhridin.nework.databinding.PostItemWithAudioBinding
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.repository.impl.MapRepositoryImpl

class PostViewHolderWithAudio(
    private val binding: PostItemWithAudioBinding,
    private val listener: PostOnItemEventsListener,
    private val activity: FragmentActivity,
    private val audioListener: VideoListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.author.text = post.author
        CoordinationControl.coordinationControl(post, binding.coordination)
        binding.contentText.text = postContent(post.content, post.author, activity.assets)
        binding.playControl.setOnClickListener { audioListener.start(post)}
        binding.likeCount.text =
            post.likeOwnerIds.size.toString()
        binding.itemSettings.isVisible = post.ownedByMe
        binding.likeBtn.isChecked = post.likedByMe
        binding.likeBtn.setOnClickListener {
            listener.onLike(post)
        }
        binding.itemSettings.setOnClickListener {
            listener.onMenuClick(post, it)
        }
        binding.userInfo.setOnClickListener {
            listener.userDetail(post.authorId)
        }
        showFullTextMoreBtn(binding.btnMore, binding.contentText, post.content)
        downloadAvatar(
            binding.authorAvatar,
            post.authorAvatar
                ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg"
        )
        binding.mentionPeople.isVisible = post.mentionIds.isNotEmpty()
        binding.mentionPeople.setOnClickListener {
            listener.onMentionPeopleClick(post)
        }
        if (!post.link.isNullOrBlank()) {
            binding.link.visibility = View.VISIBLE
            binding.link.text = post.link
        } else {
            binding.link.visibility = View.GONE
        }
        binding.published.text = post.published
    }
}

fun downloadAudio(
    context: Context,
    url: String,
    player: CustomMediaPlayer,
    playerView: PlayerView,
    volumeController: MaterialButton?,
    playControl: MaterialButton?,
    clicked: View
) {
    player.apply {
        download(url)
        clicked.setOnClickListener {
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

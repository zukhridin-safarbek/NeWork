package kg.zukhridin.nework.adapter.viewholders

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.zukhridin.nework.adapter.PostOnItemEventsListener
import kg.zukhridin.nework.adapter.downloadAvatar
import kg.zukhridin.nework.adapter.downloadImage
import kg.zukhridin.nework.adapter.postContent
import kg.zukhridin.nework.custom.CoordinationControl.Companion.coordinationControl
import kg.zukhridin.nework.databinding.PostItemWithVideoBinding
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.utils.PostMenu
import kg.zukhridin.nework.utils.PostMenuOnItemClick

interface VideoListener {
    fun start(post: Post)
}

class PostViewHolderWithVideo(
    private val binding: PostItemWithVideoBinding,
    private val listener: PostOnItemEventsListener,
    private val activity: FragmentActivity,
    private val player: ExoPlayer,
    private val videoListener: VideoListener
) : RecyclerView.ViewHolder(binding.root), Player.Listener {

    fun bind(post: Post) {
        binding.author.text = post.author
        coordinationControl(post, binding.coordination)
        binding.contentText.text = postContent(post.content, post.author, activity.assets)
        binding.likeCount.text =
            post.likeOwnerIds.size.toString()
        binding.itemSettings.isVisible = post.ownedByMe
        binding.likeBtn.isChecked = post.likedByMe
        binding.likeBtn.setOnClickListener {
            listener.onLike(post)
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
        binding.userInfo.setOnClickListener {
            listener.userDetail(post.authorId)
        }
        if (!post.link.isNullOrBlank()) {
            binding.link.visibility = View.VISIBLE
            binding.link.text = post.link
        } else {
            binding.link.visibility = View.GONE
        }
        binding.itemSettings.setOnClickListener {
            listener.onMenuClick(post, it)
        }
        binding.published.text = post.published
        Glide.with(binding.videoThumbnail).load(post.attachment?.url).centerCrop()
            .into(binding.videoThumbnail)
        binding.playControl.setOnClickListener {
            videoListener.start(post)
        }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
        player.stop()
    }
}


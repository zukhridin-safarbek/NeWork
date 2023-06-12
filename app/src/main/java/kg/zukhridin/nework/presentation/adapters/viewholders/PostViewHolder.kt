package kg.zukhridin.nework.presentation.adapters.viewholders

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.zukhridin.nework.data.util.Constants.AUTHOR_AVATAR
import kg.zukhridin.nework.databinding.PostItemBinding
import kg.zukhridin.nework.domain.enums.AttachmentType
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.presentation.listener.PostItemEventClickListener
import kg.zukhridin.nework.presentation.utils.CoordinationControl
import kg.zukhridin.nework.presentation.utils.CustomOffsetDateTime
import kg.zukhridin.nework.presentation.utils.avatarControl
import kg.zukhridin.nework.presentation.utils.postSetAuthorNameToContent
import kg.zukhridin.nework.presentation.utils.showFullTextMoreBtn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostViewHolder(
    private val binding: PostItemBinding,
    private val listener: PostItemEventClickListener,
    private val activity: FragmentActivity,
    private val mediaListener: MediaListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        postHeader(post)
        postBody(post)
        postFooter(post)
        postFooterWithoutMedia(post)
        mediaPlay(post)

    }

    private fun postFooterWithoutMedia(post: Post) {
        binding.postFooterWithoutMedia.contentTextWithoutMedia.text =
            postSetAuthorNameToContent(post.content, post.author, activity.assets)
        binding.postFooterWithoutMedia.likeTv.text = post.likeOwnerIds.size.toString()
        binding.postFooterWithoutMedia.likeBtn.isChecked = post.likedByMe
        binding.postFooterWithoutMedia.published.text = CustomOffsetDateTime.timeDecode(post.published)
        binding.postFooterWithoutMedia.link.text = post.link
        showFullTextMoreBtn(
            binding.postFooter.btnMore,
            binding.postFooter.contentText,
            post.content
        )
        binding.postFooterWithoutMedia.mentionPeople.setOnClickListener {
            listener.onMentionPeopleClick(post)
        }
        binding.postFooterWithoutMedia.likeBtn.setOnClickListener {
            listener.onLike(post)
        }
        binding.postFooterWithoutMedia.link.isVisible = !post.link.isNullOrBlank()
        binding.postFooterWithoutMedia.mentionPeople.isVisible = post.mentionIds.isNotEmpty()
    }

    private fun postFooter(post: Post) {
        binding.postFooter.mentionPeople.setOnClickListener {
            listener.onMentionPeopleClick(post)
        }
        binding.postFooter.likeBtn.setOnClickListener {
            listener.onLike(post)
        }
        binding.postFooter.link.isVisible = !post.link.isNullOrBlank()
        binding.postFooter.mentionPeople.isVisible = post.mentionIds.isNotEmpty()
        binding.postFooter.contentText.text =
            postSetAuthorNameToContent(post.content, post.author, activity.assets)
        binding.postFooter.likeTv.text = post.likeOwnerIds.size.toString()
        binding.postFooter.likeBtn.isChecked = post.likedByMe
        binding.postFooter.published.text = CustomOffsetDateTime.timeDecode(post.published)
        binding.postFooter.link.text = post.link
        showFullTextMoreBtn(
            binding.postFooter.btnMore,
            binding.postFooter.contentText,
            post.content
        )

    }

    private fun postBody(post: Post) {
        if (post.attachment != null) {
            when (post.attachment!!.type) {
                AttachmentType.VIDEO -> {
                    videoControl(post)
                    binding.postVideo.postVideoItem.visibility = View.VISIBLE
                    binding.postAudio.postAudioItem.visibility = View.GONE
                    binding.postImage.postImageItem.visibility = View.GONE
                    binding.postFooter.postFooter.visibility = View.VISIBLE
                    binding.postFooterWithoutMedia.postFooterWithoutMediaItem.visibility = View.GONE
                }
                AttachmentType.AUDIO -> {
                    binding.postVideo.postVideoItem.visibility = View.GONE
                    binding.postAudio.postAudioItem.visibility = View.VISIBLE
                    binding.postImage.postImageItem.visibility = View.GONE
                    binding.postFooter.postFooter.visibility = View.VISIBLE
                    binding.postFooterWithoutMedia.postFooterWithoutMediaItem.visibility = View.GONE
                }
                AttachmentType.IMAGE -> {
                    imageControl(post)
                    binding.postVideo.postVideoItem.visibility = View.GONE
                    binding.postAudio.postAudioItem.visibility = View.GONE
                    binding.postImage.postImageItem.visibility = View.VISIBLE
                    binding.postFooter.postFooter.visibility = View.VISIBLE
                    binding.postFooterWithoutMedia.postFooterWithoutMediaItem.visibility = View.GONE
                }
            }
        } else {
            binding.postVideo.postVideoItem.visibility = View.GONE
            binding.postAudio.postAudioItem.visibility = View.GONE
            binding.postImage.postImageItem.visibility = View.GONE
            binding.postFooter.postFooter.visibility = View.GONE
            binding.postFooterWithoutMedia.postFooterWithoutMediaItem.visibility = View.VISIBLE
        }
    }

    private fun postHeader(post: Post) {
        binding.postHeader.author.text = post.author
        avatarControl(
            binding.postHeader.authorAvatar,
            post.authorAvatar
                ?: AUTHOR_AVATAR
        )
        CoordinationControl.postCoordinationControl(post, binding.postHeader.coordination)
        binding.postHeader.postHeader.setOnClickListener {
            listener.userDetail(post.authorId)
        }
        binding.postHeader.itemSettings.setOnClickListener {
            listener.onMenuClick(post, it)
        }
        binding.postHeader.itemSettings.isVisible = post.ownedByMe
    }


    private fun videoControl(post: Post) {
        Glide.with(binding.postVideo.videoThumbnail).load(post.attachment?.url).centerCrop()
            .into(binding.postVideo.videoThumbnail)
    }

    private fun imageControl(post: Post) {
        Glide.with(binding.postImage.contentImage).load(post.attachment?.url).centerCrop()
            .into(binding.postImage.contentImage)
    }

    private fun mediaPlay(post: Post) {
        if (post.attachment?.type == AttachmentType.AUDIO) {
            binding.postAudio.playAudioControl.setOnClickListener {
                mediaListener.play(post)
            }
        } else if (post.attachment?.type == AttachmentType.VIDEO) {
            binding.postVideo.playVideoControl.setOnClickListener {
                mediaListener.play(post)
            }
        }
    }
}

interface MediaListener {
    fun play(value: Any)
}
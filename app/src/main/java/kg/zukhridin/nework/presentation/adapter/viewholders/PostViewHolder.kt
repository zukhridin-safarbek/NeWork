package kg.zukhridin.nework.presentation.adapter.viewholders

import android.view.View
import android.webkit.URLUtil
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.zukhridin.nework.data.util.Constants.AUTHOR_AVATAR
import kg.zukhridin.nework.domain.enums.AttachmentType
import kg.zukhridin.nework.presentation.utils.CoordinationControl
import kg.zukhridin.nework.databinding.PostItemBinding
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.presentation.listener.PostItemEventClickListener
import kg.zukhridin.nework.presentation.utils.avatarControl
import kg.zukhridin.nework.presentation.utils.postSetAuthorNameToContent
import kg.zukhridin.nework.presentation.utils.showFullTextMoreBtn

class PostViewHolder(
    private val binding: PostItemBinding,
    private val listener: PostItemEventClickListener,
    private val activity: FragmentActivity,
    private val mediaListener: MediaListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.author.text = post.author
        binding.contentText.text =
            postSetAuthorNameToContent(post.content, post.author, activity.assets)
        binding.likeTv.text = post.likeOwnerIds.size.toString()
        binding.likeBtn.isChecked = post.likedByMe
        binding.published.text = post.published
        binding.link.text = post.link
        showFullTextMoreBtn(binding.btnMore, binding.contentText, post.content)
        avatarControl(
            binding.authorAvatar,
            post.authorAvatar
                ?: AUTHOR_AVATAR
        )
        mediaTvVisibility(post)
        viewsVisibility(post)
        buttonsOnClick(post)
        mediaPlay(post)
        CoordinationControl.postCoordinationControl(post, binding.coordination)
    }

    private fun mediaTvVisibility(post: Post) = with(binding) {
        if (post.attachment != null && URLUtil.isValidUrl(post.attachment!!.url)) {
            when (post.attachment!!.type) {
                AttachmentType.IMAGE -> {
                    contentImageMediaContainer.visibility = View.VISIBLE
                    contentAudioMediaContainer.visibility = View.GONE
                    contentVideoMediaContainer.visibility = View.GONE
                    imageControl(post)
                    layoutParamsWithMedia()
                }
                AttachmentType.VIDEO -> {
                    contentVideoMediaContainer.visibility = View.VISIBLE
                    contentImageMediaContainer.visibility = View.GONE
                    contentAudioMediaContainer.visibility = View.GONE
                    videoControl(post)
                    layoutParamsWithMedia()
                }
                AttachmentType.AUDIO -> {
                    contentAudioMediaContainer.visibility = View.VISIBLE
                    contentVideoMediaContainer.visibility = View.GONE
                    contentImageMediaContainer.visibility = View.GONE
                    layoutParamsWithMedia()
                }
                else -> {
                    contentAudioMediaContainer.visibility = View.GONE
                    contentVideoMediaContainer.visibility = View.GONE
                    contentImageMediaContainer.visibility = View.GONE
                    layoutParamsWithOutMedia()
                }
            }
        } else {
            contentAudioMediaContainer.visibility = View.GONE
            contentVideoMediaContainer.visibility = View.GONE
            contentImageMediaContainer.visibility = View.GONE
            layoutParamsWithOutMedia()
        }
    }

    private fun layoutParamsWithMedia() {
        binding.containerContentText.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToTop = ConstraintLayout.LayoutParams.UNSET
            topToBottom = binding.eventsBarrier.id
        }
        binding.likeBtn.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToBottom = ConstraintLayout.LayoutParams.UNSET
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            setMargins(0, 4, 0, 0)
        }
        binding.link.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToBottom = binding.containerContentText.id
        }
    }

    private fun layoutParamsWithOutMedia() {
        binding.containerContentText.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = ConstraintLayout.LayoutParams.UNSET
        }
        binding.likeBtn.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToBottom = binding.containerContentText.id
            topToTop = ConstraintLayout.LayoutParams.UNSET
            setMargins(0, 4, 0, 0)
        }
        binding.link.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToBottom = binding.likeBtn.id
        }
    }

    private fun videoControl(post: Post) {
        Glide.with(binding.videoThumbnail).load(post.attachment?.url).centerCrop()
            .into(binding.videoThumbnail)
    }

    private fun imageControl(post: Post) {
        Glide.with(binding.contentImage).load(post.attachment?.url).centerCrop()
            .into(binding.contentImage)
    }

    private fun viewsVisibility(post: Post) = with(binding) {
        link.isVisible = !post.link.isNullOrBlank()
        mentionPeople.isVisible = post.mentionIds.isNotEmpty()
        itemSettings.isVisible = post.ownedByMe
    }

    private fun mediaPlay(post: Post) {
        if (post.attachment?.type == AttachmentType.AUDIO) {
            binding.playAudioControl.setOnClickListener {
                mediaListener.play(post)
            }
        } else if (post.attachment?.type == AttachmentType.VIDEO) {
            binding.playVideoControl.setOnClickListener {
                mediaListener.play(post)
            }
        }
    }

    private fun buttonsOnClick(post: Post) {
        binding.header.setOnClickListener {
            listener.userDetail(post.authorId)
        }
        binding.itemSettings.setOnClickListener {
            listener.onMenuClick(post, it)
        }
        binding.mentionPeople.setOnClickListener {
            listener.onMentionPeopleClick(post)
        }
        binding.likeBtn.setOnClickListener {
            listener.onLike(post)
        }
    }
}

interface MediaListener {
    fun play(post: Post)
}
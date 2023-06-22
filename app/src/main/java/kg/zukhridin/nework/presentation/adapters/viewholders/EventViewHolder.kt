package kg.zukhridin.nework.presentation.adapters.viewholders

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.zukhridin.nework.data.util.Constants.AUTHOR_AVATAR
import kg.zukhridin.nework.databinding.EventItemBinding
import kg.zukhridin.nework.domain.enums.AttachmentType
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.presentation.listener.EventItemClickListener
import kg.zukhridin.nework.presentation.utils.*

class EventViewHolder(
    private val binding: EventItemBinding,
    private val listener: EventItemClickListener,
    private val mediaListener: MediaListener,
    private val activity: FragmentActivity,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(event: Event) {
        eventHeader(event)
        eventBody(event)
        eventFooter(event)
        eventFooterWithoutMedia(event)
        mediaPlay(event)
    }

    private fun eventFooterWithoutMedia(event: Event) {
        binding.postFooterWithoutMedia.contentTextWithoutMedia.text =
            postSetAuthorNameToContent(event.content, event.author, activity.assets)
        binding.postFooterWithoutMedia.likeTv.text = event.likeOwnerIds.size.toString()
        binding.postFooterWithoutMedia.likeBtn.isChecked = event.likedByMe
        binding.postFooterWithoutMedia.mentionPeople.visibility = View.GONE
        binding.postFooterWithoutMedia.published.text = event.published
        binding.postFooterWithoutMedia.link.text = event.link
        showFullTextMoreBtn(
            binding.postFooter.btnMore,
            binding.postFooter.contentText,
            event.content
        )
        binding.postFooterWithoutMedia.likeBtn.setOnClickListener {
            listener.onLike(event)
        }
        binding.postFooterWithoutMedia.link.isVisible = !event.link.isNullOrBlank()
    }

    private fun eventFooter(event: Event) {
        binding.postFooter.likeBtn.setOnClickListener {
            listener.onLike(event)
        }
        binding.postFooter.link.isVisible = !event.link.isNullOrBlank()
        binding.postFooter.mentionPeople.visibility = View.GONE
        binding.postFooter.contentText.text =
            postSetAuthorNameToContent(event.content, event.author, activity.assets)
        binding.postFooter.likeTv.text = event.likeOwnerIds.size.toString()
        binding.postFooter.likeBtn.isChecked = event.likedByMe
        binding.postFooter.published.text = event.published
        binding.postFooter.link.text = event.link
        showFullTextMoreBtn(
            binding.postFooter.btnMore,
            binding.postFooter.contentText,
            event.content
        )

    }

    private fun eventBody(event: Event) {
        if (event.attachment != null) {
            when (event.attachment!!.type) {
                AttachmentType.VIDEO -> {
                    videoControl(event)
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
                    imageControl(event)
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

    private fun eventHeader(event: Event) {
        binding.postHeader.author.text = event.author
        avatarControl(
            binding.postHeader.authorAvatar,
            event.authorAvatar
                ?: AUTHOR_AVATAR
        )
        if (event.coords != null){
            CoordinationControl.eventCoordinationControl(event, binding.postHeader.coordination)
        }else{
            binding.postHeader.coordination.visibility = View.GONE
        }
        binding.postHeader.itemSettings.setOnClickListener {
            listener.onMenuClick(event, it)
        }
        binding.postHeader.itemSettings.isVisible = event.ownedByMe
    }


    private fun videoControl(event: Event) {
        Glide.with(binding.postVideo.videoThumbnail).load(event.attachment?.url).centerCrop()
            .into(binding.postVideo.videoThumbnail)
    }

    private fun imageControl(event: Event) {
        Glide.with(binding.postImage.contentImage).load(event.attachment?.url).centerCrop()
            .into(binding.postImage.contentImage)
    }

    private fun mediaPlay(event: Event) {
        if (event.attachment?.type == AttachmentType.AUDIO) {
            binding.postAudio.playAudioControl.setOnClickListener {
                mediaListener.play(event)
            }
        } else if (event.attachment?.type == AttachmentType.VIDEO) {
            binding.postVideo.playVideoControl.setOnClickListener {
                mediaListener.play(event)
            }
        }
    }

}
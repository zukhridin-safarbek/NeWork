package kg.zukhridin.nework.presentation.adapter.viewholders

import android.view.View
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.zukhridin.nework.databinding.EventItemBinding
import kg.zukhridin.nework.presentation.utils.CoordinationControl
import kg.zukhridin.nework.data.util.Constants.AUTHOR_AVATAR
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.presentation.utils.CustomOffsetDateTime
import kg.zukhridin.nework.presentation.listener.EventItemClickListener

class EventViewHolder(
    private val binding: EventItemBinding,
    private val eventItemClickListener: EventItemClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(event: Event) {
        if (event.attachment != null && URLUtil.isValidUrl(event.attachment!!.url)) {
            binding.textContentWithOutMedia.visibility = View.GONE
            binding.textContentWithMedia.visibility = View.VISIBLE
            with(binding) {
                author.text = event.author
                Glide.with(binding.authorAvatar).load(event.authorAvatar ?: AUTHOR_AVATAR).circleCrop()
                    .into(binding.authorAvatar)
                contentText.text = event.content
                link.text = event.link
                published.text = event.published?.let { CustomOffsetDateTime.timeDecode(it) }
                likeTv.text =
                    if (event.likeOwnerIds.isEmpty()) "0" else event.likeOwnerIds.size.toString()
                likeBtn.isChecked = event.likedByMe
                likeBtn.setOnClickListener {
                    eventItemClickListener.onLike(event = event)
                }
                CoordinationControl.eventCoordinationControl(event, coordination)
                if (event.link == null) {
                    link.visibility = View.GONE
                } else {
                    link.visibility = View.VISIBLE
                }
                binding.itemSettings.setOnClickListener {
                    eventItemClickListener.onMenuClick(event = event, it)
                }
            }
            Glide.with(binding.contentImage).load(event.attachment!!.url)
                .into(binding.contentImage)
        } else {
           binding.textContentWithOutMedia.visibility = View.VISIBLE
           binding.textContentWithMedia.visibility = View.GONE
            with(binding) {
                author.text = event.author
                Glide.with(binding.authorAvatar).load(event.authorAvatar ?: AUTHOR_AVATAR).circleCrop()
                    .into(binding.authorAvatar)
                contentTextWithoutMedia.text = event.content
                linkWithoutMedia.text = event.link
                publishedWithoutMedia.text = event.published?.let { CustomOffsetDateTime.timeDecode(it) }
                likeTvWithoutMedia.text =
                    if (event.likeOwnerIds.isEmpty()) "0" else event.likeOwnerIds.size.toString()
                likeBtnWithoutMedia.isChecked = event.likedByMe
                likeBtnWithoutMedia.setOnClickListener {
                    eventItemClickListener.onLike(event = event)
                }
                CoordinationControl.eventCoordinationControl(event, coordination)
                if (event.link == null) {
                    linkWithoutMedia.visibility = View.GONE
                } else {
                    linkWithoutMedia.visibility = View.VISIBLE
                }
                binding.itemSettings.setOnClickListener {
                    eventItemClickListener.onMenuClick(event = event, it)
                }
            }
        }
    }
}
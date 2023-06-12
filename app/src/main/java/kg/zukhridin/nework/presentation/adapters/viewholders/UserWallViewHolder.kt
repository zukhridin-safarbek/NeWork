package kg.zukhridin.nework.presentation.adapters.viewholders

import android.view.View
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.zukhridin.nework.data.util.Constants.AUDIO_IMAGE
import kg.zukhridin.nework.data.util.Constants.NO_MEDIA_IMAGE
import kg.zukhridin.nework.domain.enums.AttachmentType
import kg.zukhridin.nework.databinding.UserDetailPostItemBinding
import kg.zukhridin.nework.domain.models.Post

class UserWallViewHolder(
    private val binding: UserDetailPostItemBinding,
    private val postItemClickListener: PostItemClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        if (URLUtil.isValidUrl(post.attachment?.url)) {
            when (post.attachment?.type) {
                AttachmentType.VIDEO -> {
                    binding.audioIcon.visibility = View.GONE
                    binding.videoIcon.visibility = View.VISIBLE
                    Glide.with(binding.contentImage).load(post.attachment?.url).into(binding.contentImage)
                }
                AttachmentType.IMAGE -> {
                    binding.audioIcon.visibility = View.GONE
                    binding.videoIcon.visibility = View.GONE
                    Glide.with(binding.contentImage).load(post.attachment?.url).into(binding.contentImage)
                }
                AttachmentType.AUDIO -> {
                    binding.audioIcon.visibility = View.VISIBLE
                    binding.videoIcon.visibility = View.GONE
                    Glide.with(binding.contentImage).load(AUDIO_IMAGE).into(binding.contentImage)
                }
                else -> {
                    binding.audioIcon.visibility = View.GONE
                    binding.videoIcon.visibility = View.GONE
                    Glide.with(binding.contentImage).load(NO_MEDIA_IMAGE).into(binding.contentImage)
                }
            }
        } else {
            binding.audioIcon.visibility = View.GONE
            binding.videoIcon.visibility = View.GONE
            Glide.with(binding.contentImage).load(NO_MEDIA_IMAGE).into(binding.contentImage)
        }
        itemView.setOnClickListener {
            postItemClickListener.postItemClick(post)
        }
    }
}

interface PostItemClickListener {
    fun postItemClick(post: Post)
}

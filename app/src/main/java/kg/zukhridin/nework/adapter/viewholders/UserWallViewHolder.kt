package kg.zukhridin.nework.adapter.viewholders

import android.view.View
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.databinding.UserDetailPostItemBinding
import kg.zukhridin.nework.dto.AttachmentType
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.model.PostItemClick

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
                    kg.zukhridin.nework.custom.Glide.start(
                        binding.contentImage,
                        post.attachment.url
                    )
                }
                AttachmentType.IMAGE -> {
                    binding.audioIcon.visibility = View.GONE
                    binding.videoIcon.visibility = View.GONE
                    kg.zukhridin.nework.custom.Glide.start(
                        binding.contentImage,
                        post.attachment.url
                    )
                }
                AttachmentType.AUDIO -> {
                    binding.audioIcon.visibility = View.VISIBLE
                    binding.videoIcon.visibility = View.GONE
                    kg.zukhridin.nework.custom.Glide.start(
                        binding.contentImage,
                        "https://www.baltana.com/files/wallpapers-30/Abstract-Heartbeat-Design-HD-Desktop-Wallpaper-100292.jpg"
                    )
                }
                else -> {
                    binding.audioIcon.visibility = View.GONE
                    binding.videoIcon.visibility = View.GONE
                    kg.zukhridin.nework.custom.Glide.start(
                        binding.contentImage,
                        "https://i.pinimg.com/originals/36/8f/b5/368fb52ce56f8748a86543f8e3a537aa.png"
                    )
                }
            }
        } else {
            binding.audioIcon.visibility = View.GONE
            binding.videoIcon.visibility = View.GONE
            kg.zukhridin.nework.custom.Glide.start(
                binding.contentImage,
                "https://i.pinimg.com/originals/36/8f/b5/368fb52ce56f8748a86543f8e3a537aa.png"
            )
        }
        itemView.setOnClickListener {
            postItemClickListener.postItemClick(post)
        }
    }
}

interface PostItemClickListener {
    fun postItemClick(post: Post)
}

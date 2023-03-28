package kg.zukhridin.nework.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.databinding.PostItemWithoutAttachmentBinding
import kg.zukhridin.nework.dto.Post

class PostViewHolderWithoutAttachment(
    private val binding: PostItemWithoutAttachmentBinding,
    private val listener: PostOnItemEventsListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.author.text = post.author
        binding.coordination.text = post.attachment?.type.toString()
        binding.contentText.text = post.content
        binding.likeCount.text =
            post.likeOwnerIds.size.toString()
        binding.likeBtn.isChecked = post.likedByMe
        binding.likeBtn.setOnClickListener {
            listener.onLike(post)
        }
        binding.itemSettings.isVisible = post.ownedByMe
        binding.commentBtn.setOnClickListener {
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
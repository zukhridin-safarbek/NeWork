package kg.zukhridin.nework.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.databinding.PostItemWithImageBinding
import kg.zukhridin.nework.dto.Post

class PostViewHolderWithImage(
    private val binding: PostItemWithImageBinding,
    private val listener: PostOnItemEventsListener,
    private val activity: FragmentActivity,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.author.text = post.author
        binding.coordination.text = post.attachment?.type.toString()
        binding.contentText.text = postContent(post.content, post.author,activity.assets)
        downloadImage(binding.contentImage, post.attachment?.url.toString())
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
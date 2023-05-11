package kg.zukhridin.nework.adapter.viewholders

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.adapter.PostOnItemEventsListener
import kg.zukhridin.nework.adapter.downloadAvatar
import kg.zukhridin.nework.adapter.downloadImage
import kg.zukhridin.nework.adapter.postContent
import kg.zukhridin.nework.custom.CoordinationControl
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
        CoordinationControl.coordinationControl(post, binding.coordination)
        binding.contentText.text = post.content
        downloadImage(binding.contentImage, post.attachment?.url.toString())
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
        binding.mentionPeople.isVisible = post.mentionIds.isNotEmpty()
        binding.mentionPeople.setOnClickListener {
            listener.onMentionPeopleClick(post)
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
        if (!post.link.isNullOrBlank()) {
            binding.link.visibility = View.VISIBLE
            binding.link.text = post.link
        } else {
            binding.link.visibility = View.GONE
        }
        itemView.setOnClickListener {
            listener.postItemClick(post)
        }
        binding.published.text = post.published
    }
}
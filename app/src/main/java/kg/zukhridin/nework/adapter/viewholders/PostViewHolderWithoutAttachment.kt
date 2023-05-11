package kg.zukhridin.nework.adapter.viewholders

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.R
import kg.zukhridin.nework.adapter.PostOnItemEventsListener
import kg.zukhridin.nework.adapter.downloadAvatar
import kg.zukhridin.nework.custom.CoordinationControl.Companion.coordinationControl
import kg.zukhridin.nework.databinding.PostItemWithoutAttachmentBinding
import kg.zukhridin.nework.dto.Post

class PostViewHolderWithoutAttachment(
    private val binding: PostItemWithoutAttachmentBinding,
    private val listener: PostOnItemEventsListener
) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(post: Post) {
        binding.author.text = post.author
        coordinationControl(post, binding.coordination)
        binding.contentText.text = post.content
        binding.likeCount.text =
            post.likeOwnerIds.size.toString()
        binding.likeBtn.isChecked = post.likedByMe
        binding.likeBtn.setOnClickListener {
            listener.onLike(post)
        }
        binding.itemSettings.isVisible = post.ownedByMe
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
        binding.published.text = post.published

    }

}

fun showFullTextMoreBtn(btnMore: TextView, contentText: TextView, string: String) {
    btnMore.setOnClickListener {
        if (contentText.maxLines == 2) {
            contentText.maxLines = Integer.MAX_VALUE
            btnMore.text = btnMore.context.getString(R.string.less)
        } else {
            contentText.maxLines = 2
            btnMore.text = btnMore.context.getString(R.string.more)
        }
    }
    val lines = string.lines().size
    contentText.viewTreeObserver.addOnGlobalLayoutListener {
        if (lines > 2 || string.length > 100) {
            btnMore.visibility = View.VISIBLE
        } else {
            btnMore.visibility = View.GONE
        }
    }
}


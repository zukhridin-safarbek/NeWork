package kg.zukhridin.nework.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.callback.PostDiffCallBack
import kg.zukhridin.nework.custom.Glide
import kg.zukhridin.nework.databinding.PostItemBinding
import kg.zukhridin.nework.databinding.PostItemWithoutAttachmentBinding
import kg.zukhridin.nework.dto.AttachmentType
import kg.zukhridin.nework.dto.Post

@SuppressLint("SetTextI18n")
class PostAdapter(private val listener: PostOnItemEventsListener) :
    PagingDataAdapter<Post, RecyclerView.ViewHolder>(PostDiffCallBack()) {
    class PostViewHolder(
        private val binding: PostItemBinding,
        private val listener: PostOnItemEventsListener,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.author.text = post.author
            binding.coordination.text = post.coords?.lat
            addMoreContentText(post.content, post.author, binding.contentText)
            if (post.attachment != null) {
                if (post.attachment.type == AttachmentType.IMAGE && URLUtil.isValidUrl(post.attachment.url)) {
                    binding.contentImage.visibility = View.VISIBLE
                    binding.contentVideo.visibility = View.GONE
                    downloadImage(binding.contentImage, post.attachment.url)
                } else if (post.attachment.type == AttachmentType.VIDEO && URLUtil.isValidUrl(post.attachment.url)) {
                    binding.contentVideo.setVideoURI(Uri.parse(post.attachment.url))
                    val mediaController = MediaController(binding.root.context)
                    mediaController.setAnchorView(binding.contentVideo)
                    binding.contentVideo.requestFocus()
                    binding.contentVideo.start()
                    binding.contentVideo.setMediaController(mediaController)
                    binding.contentVideo.visibility = View.VISIBLE
                    binding.contentImage.visibility = View.GONE
                } else {
                    binding.contentVideo.visibility = View.GONE
                    binding.contentImage.visibility = View.GONE
                }
            }
            binding.likeCount.text =
                if (post.likeOwnerIds == null) (0L).toString() else post.likeOwnerIds.size.toString()
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
                binding.link.text = post.link
            } else {
                binding.link.visibility = View.GONE
            }
            binding.published.text = post.published
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val post = getItem(position)) {
            is Post -> {
                if (post.attachment != null && post.attachment.type == AttachmentType.IMAGE && URLUtil.isValidUrl(
                        post.attachment.url
                    )
                ) (holder as? PostViewHolder)?.bind(post) else (holder as? PostViewHolderWithoutAttachment)?.bind(
                    post
                )
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        when (val post = getItem(position)) {
            is Post -> {
                return if (post.attachment != null && post.attachment.type == AttachmentType.IMAGE && URLUtil.isValidUrl(
                        post.attachment.url
                    )
                ) 1 else 0
            }
            else -> error("Unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val binding =
                    PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, listener)
            }
            0 -> {
                val binding =
                    PostItemWithoutAttachmentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                PostViewHolderWithoutAttachment(binding, listener)
            }
            else -> {
                error("Unknown item type")
            }
        }
    }

    class PostViewHolderWithoutAttachment(
        private val binding: PostItemWithoutAttachmentBinding,
        private val listener: PostOnItemEventsListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.author.text = post.author
            binding.coordination.text = post.coords?.lat
            addMoreContentText(content = post.content, author = "", textView = binding.contentText)
            binding.likeCount.text =
                if (post.likeOwnerIds == null) (0L).toString() else post.likeOwnerIds.size.toString()
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
                binding.link.text = post.link
            } else {
                binding.link.visibility = View.GONE
            }
            binding.published.text = post.published
        }
    }


}

private fun addMoreContentText(
    content: String,
    author: String,
    textView: TextView
) {
    content.split(" ")
    val more = SpannableStringBuilder(author)
    val colorOwn = ForegroundColorSpan(Color.argb(255, 0, 0, 0))
    val size = RelativeSizeSpan(1.1f)
    if (author.isNotBlank()) {
        more.append(": ")
        more.append(content)
        more.setSpan(colorOwn, 0, author.length + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        more.setSpan(size, 0, author.length + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    } else {
        more.append(content)
    }
    if (textView.lineCount > 2) {
        more.append(" more")
        val contentP = object : ClickableSpan() {
            override fun onClick(widget: View) {
                println("Click")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.apply {
                    isUnderlineText = false
                    textSize = 34f
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    color = Color.argb(255, 115, 115, 155)
                }
            }
        }
        more.setSpan(contentP, more.length - 4, more.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = more
    } else {
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = more
    }

}

private fun downloadImage(image: ImageView, avatar: String) {
    Glide.start(image, avatar)
}

interface PostOnItemEventsListener {
    fun onLike(post: Post)
}
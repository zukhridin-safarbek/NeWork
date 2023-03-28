package kg.zukhridin.nework.adapter

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.callback.PostDiffCallBack
import kg.zukhridin.nework.custom.CustomMediaPlayer
import kg.zukhridin.nework.custom.Glide
import kg.zukhridin.nework.databinding.PostItemWithAudioBinding
import kg.zukhridin.nework.databinding.PostItemWithImageBinding
import kg.zukhridin.nework.databinding.PostItemWithVideoBinding
import kg.zukhridin.nework.databinding.PostItemWithoutAttachmentBinding
import kg.zukhridin.nework.dto.AttachmentType
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.utils.AUDIO
import kg.zukhridin.nework.utils.IMAGE
import kg.zukhridin.nework.utils.VIDEO
import kg.zukhridin.nework.utils.WITHOUT_ATTACHMENT


@SuppressLint("SetTextI18n")
class PostAdapter(
    private val listener: PostOnItemEventsListener,
    private val activity: FragmentActivity
) :
    PagingDataAdapter<Post, RecyclerView.ViewHolder>(PostDiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        when (val post = getItem(position)) {
            is Post -> {
                return if (post.attachment != null && URLUtil.isValidUrl(
                        post.attachment.url
                    ) && post.attachment.type == AttachmentType.IMAGE
                ) IMAGE else if (post.attachment != null && URLUtil.isValidUrl(
                        post.attachment.url
                    ) && post.attachment.type == AttachmentType.VIDEO
                ) VIDEO else if (post.attachment != null && URLUtil.isValidUrl(
                        post.attachment.url
                    ) && post.attachment.type == AttachmentType.AUDIO
                ) AUDIO else WITHOUT_ATTACHMENT
            }
            else -> error("Unknown item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val post = getItem(position)) {
            is Post -> {
                if (post.attachment != null && URLUtil.isValidUrl(
                        post.attachment.url
                    ) && post.attachment.type == AttachmentType.IMAGE
                ) (holder as? PostViewHolderWithImage)?.bind(post)
                else if (post.attachment != null && URLUtil.isValidUrl(
                        post.attachment.url
                    ) && post.attachment.type == AttachmentType.VIDEO
                ) (holder as? PostViewHolderWithVideo)?.bind(post)
                else if (post.attachment != null && URLUtil.isValidUrl(
                        post.attachment.url
                    ) && post.attachment.type == AttachmentType.AUDIO
                ) (holder as? PostViewHolderWithAudio)?.bind(post)
                else (holder as? PostViewHolderWithoutAttachment)?.bind(
                    post
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IMAGE -> {
                val binding =
                    PostItemWithImageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                PostViewHolderWithImage(binding, listener, activity)
            }
            VIDEO -> {
                val binding =
                    PostItemWithVideoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val videoPlayer =
                    CustomMediaPlayer(parent.context, binding.contentVideo, AttachmentType.VIDEO)
                PostViewHolderWithVideo(binding, listener, activity, videoPlayer)
            }
            AUDIO -> {
                val binding = PostItemWithAudioBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val audioPlayer =
                    CustomMediaPlayer(parent.context, binding.contentAudio, AttachmentType.AUDIO)
                PostViewHolderWithAudio(binding, listener, activity, audioPlayer)
            }
            WITHOUT_ATTACHMENT -> {
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
}

fun downloadImage(image: ImageView, avatar: String) {
    Glide.start(image, avatar)
}

fun postContent(
    content: String,
    author: String,
    assets: AssetManager
): SpannableStringBuilder {
    val ssb = SpannableStringBuilder()
    ssb.append("$author ")
    val authorDraw = object : ClickableSpan() {
        override fun onClick(widget: View) = Unit

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = Color.BLACK
            ds.typeface =
                Typeface.createFromAsset(assets, "fonts/philosopher_regular.ttf")
        }
    }
    ssb.append(content)
    ssb.setSpan(authorDraw, 0, author.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    return ssb
}

interface PostOnItemEventsListener {
    fun onLike(post: Post)
}
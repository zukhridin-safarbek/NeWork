package kg.zukhridin.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.callback.PostDiffCallBack
import kg.zukhridin.nework.custom.Glide
import kg.zukhridin.nework.databinding.WallGalleryWithAttachmentItemBinding
import kg.zukhridin.nework.dto.Post

class WallWithAttachmentGalleryAdapter() :
    ListAdapter<Post, WallWithAttachmentGalleryAdapter.ViewHolder>(PostDiffCallBack()) {
    class ViewHolder(private val binding: WallGalleryWithAttachmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
//            if (post.attachment != null && URLUtil.isValidUrl(post.attachment.url)) {
                Glide.start(binding.root, post.attachment?.url ?: "https://ggdt.ru/file/2020/01/pudge-ethreain-lich-crixalis.jpg")
//            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WallGalleryWithAttachmentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}
package kg.zukhridin.nework.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.zukhridin.nework.callback.GalleryDiffCallBack
import kg.zukhridin.nework.custom.CustomMediaPlayer
import kg.zukhridin.nework.databinding.GalleryAudiosItemBinding
import kg.zukhridin.nework.databinding.GalleryImagesItemBinding
import kg.zukhridin.nework.databinding.GalleryVideosItemBinding
import kg.zukhridin.nework.dto.AttachmentType
import kg.zukhridin.nework.dto.CustomMedia
import kg.zukhridin.nework.dto.CustomMediaType
import kg.zukhridin.nework.utils.AUDIO
import kg.zukhridin.nework.utils.IMAGE
import kg.zukhridin.nework.utils.VIDEO
import java.io.File

interface OnImageItemClickListener {
    fun itemClick(customMedia: CustomMedia)
}

class GalleryAdapter(
    private val customMedia: ArrayList<CustomMedia>,
    private val listener: OnImageItemClickListener
) :
    ListAdapter<CustomMedia, RecyclerView.ViewHolder>(GalleryDiffCallBack()) {
    class GalleryImageViewHolder(
        private val binding: GalleryImagesItemBinding,
        private val listener: OnImageItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: CustomMedia) {
            val file = File(image.url)
            val uri = Uri.fromFile(file)
            Glide.with(binding.image).load(uri).into(binding.image)
            binding.image.setOnClickListener {
                listener.itemClick(
                    image
                )
            }
        }
    }

    class GalleryVideoViewHolder(
        private val binding: GalleryVideosItemBinding,
        private val listener: OnImageItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: CustomMedia) {
            Glide.with(binding.thumbnail).load(video.url).into(binding.thumbnail)
            binding.thumbnail.setOnClickListener {
                listener.itemClick(video)
            }
        }
    }

    class GalleryAudioViewHolder(
        private val binding: GalleryAudiosItemBinding,
        private val listener: OnImageItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: CustomMedia) {
            Glide.with(binding.audios).load(image.url)
                .into(binding.audios)
            binding.audios.setOnClickListener {
                listener.itemClick(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IMAGE -> {
                val binding = GalleryImagesItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GalleryImageViewHolder(binding, listener)
            }
            VIDEO -> {
                val binding = GalleryVideosItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GalleryVideoViewHolder(binding, listener)
            }
            AUDIO -> {
                val binding = GalleryAudiosItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GalleryAudioViewHolder(binding, listener)
            }
            else -> {
                throw Exception()
            }
        }
    }

    override fun getItemCount(): Int {
        return customMedia.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (customMedia[position].type) {
            CustomMediaType.IMAGE -> IMAGE
            CustomMediaType.VIDEO -> VIDEO
            CustomMediaType.AUDIO -> AUDIO
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = customMedia[position]
        when (holder.itemViewType) {
            IMAGE -> {
                (holder as GalleryImageViewHolder).bind(item)
            }
            VIDEO -> (holder as GalleryVideoViewHolder).bind(item)
            AUDIO -> (holder as GalleryAudioViewHolder).bind(item)
        }
    }
}
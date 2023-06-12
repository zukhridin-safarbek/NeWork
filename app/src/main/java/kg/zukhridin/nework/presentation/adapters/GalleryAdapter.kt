package kg.zukhridin.nework.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.zukhridin.nework.domain.models.MediaModel
import kg.zukhridin.nework.databinding.GalleryAudiosItemBinding
import kg.zukhridin.nework.data.util.Constants.AUDIO
import kg.zukhridin.nework.data.util.Constants.IMAGE
import kg.zukhridin.nework.data.util.Constants.VIDEO
import kg.zukhridin.nework.domain.enums.MediaType
import kg.zukhridin.nework.presentation.adapters.callback.GalleryDiffCallBack
import kg.zukhridin.nework.databinding.GalleryImagesItemBinding
import kg.zukhridin.nework.databinding.GalleryVideosItemBinding
import java.io.File

interface OnImageItemClickListener {
    fun itemClick(mediaModel: MediaModel)
}

class GalleryAdapter(
    private val mediaModel: ArrayList<MediaModel>,
    private val listener: OnImageItemClickListener
) :
    ListAdapter<MediaModel, RecyclerView.ViewHolder>(GalleryDiffCallBack()) {
    class GalleryImageViewHolder(
        private val binding: GalleryImagesItemBinding,
        private val listener: OnImageItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: MediaModel) {
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

        fun bind(video: MediaModel) {
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

        fun bind(image: MediaModel) {
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
                GalleryImageViewHolder(
                    binding,
                    listener
                )
            }
            VIDEO -> {
                val binding = GalleryVideosItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GalleryVideoViewHolder(
                    binding,
                    listener
                )
            }
            AUDIO -> {
                val binding = GalleryAudiosItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GalleryAudioViewHolder(
                    binding,
                    listener
                )
            }
            else -> {
                throw Exception()
            }
        }
    }

    override fun getItemCount(): Int {
        return mediaModel.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (mediaModel[position].type) {
            MediaType.IMAGE -> IMAGE
            MediaType.VIDEO -> VIDEO
            MediaType.AUDIO -> AUDIO
            else -> -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mediaModel[position]
        when (holder.itemViewType) {
            IMAGE -> {
                (holder as GalleryImageViewHolder).bind(item)
            }
            VIDEO -> (holder as GalleryVideoViewHolder).bind(item)
            AUDIO -> (holder as GalleryAudioViewHolder).bind(item)
        }
    }
}
package kg.zukhridin.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.callback.GalleryDiffCallBack
import kg.zukhridin.nework.databinding.FragmentNewPostGalleryBinding
import kg.zukhridin.nework.dto.Image

class GalleryAdapter(private val images: List<Image>) :
    ListAdapter<Image, GalleryAdapter.GalleryViewHolder>(GalleryDiffCallBack()) {
    class GalleryViewHolder(private val binding: FragmentNewPostGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = FragmentNewPostGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GalleryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
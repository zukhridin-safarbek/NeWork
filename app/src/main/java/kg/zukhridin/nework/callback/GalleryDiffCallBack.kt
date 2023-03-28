package kg.zukhridin.nework.callback

import androidx.recyclerview.widget.DiffUtil
import kg.zukhridin.nework.dto.Image

class GalleryDiffCallBack: DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}
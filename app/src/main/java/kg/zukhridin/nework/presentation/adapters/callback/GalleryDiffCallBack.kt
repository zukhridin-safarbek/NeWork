package kg.zukhridin.nework.presentation.adapters.callback

import androidx.recyclerview.widget.DiffUtil
import kg.zukhridin.nework.domain.models.MediaModel

class GalleryDiffCallBack: DiffUtil.ItemCallback<MediaModel>() {
    override fun areItemsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean {
        return oldItem == newItem
    }
}
package kg.zukhridin.nework.callback

import androidx.recyclerview.widget.DiffUtil
import kg.zukhridin.nework.dto.CustomMedia

class GalleryDiffCallBack: DiffUtil.ItemCallback<CustomMedia>() {
    override fun areItemsTheSame(oldItem: CustomMedia, newItem: CustomMedia): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: CustomMedia, newItem: CustomMedia): Boolean {
        return oldItem == newItem
    }
}
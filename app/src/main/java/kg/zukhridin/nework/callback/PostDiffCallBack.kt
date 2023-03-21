package kg.zukhridin.nework.callback

import androidx.recyclerview.widget.DiffUtil
import kg.zukhridin.nework.dto.Post

class PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem.id == newItem.id

}

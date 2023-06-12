package kg.zukhridin.nework.presentation.adapters.callback

import androidx.recyclerview.widget.DiffUtil
import kg.zukhridin.nework.domain.models.Post

class PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: Post, newItem: Post): Any {
        return Unit
    }
}

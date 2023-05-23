package kg.zukhridin.nework.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.databinding.UserDetailPostItemBinding
import kg.zukhridin.nework.presentation.adapter.callback.PostDiffCallBack
import kg.zukhridin.nework.presentation.adapter.viewholders.PostItemClickListener
import kg.zukhridin.nework.presentation.adapter.viewholders.UserWallViewHolder

class UserWallAdapter(
    private val listener: PostItemClickListener
) : ListAdapter<Post, UserWallViewHolder>(PostDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserWallViewHolder {
        val binding =
            UserDetailPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserWallViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: UserWallViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}
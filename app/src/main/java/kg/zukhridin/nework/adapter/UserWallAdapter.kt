package kg.zukhridin.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.adapter.viewholders.PostItemClickListener
import kg.zukhridin.nework.adapter.viewholders.UserWallViewHolder
import kg.zukhridin.nework.callback.PostDiffCallBack
import kg.zukhridin.nework.databinding.UserDetailPostItemBinding
import kg.zukhridin.nework.dto.Post

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
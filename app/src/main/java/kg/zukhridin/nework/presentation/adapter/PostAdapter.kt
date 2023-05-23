package kg.zukhridin.nework.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagingDataAdapter
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.databinding.PostItemBinding
import kg.zukhridin.nework.presentation.adapter.callback.PostDiffCallBack
import kg.zukhridin.nework.presentation.adapter.viewholders.MediaListener
import kg.zukhridin.nework.presentation.adapter.viewholders.PostViewHolder
import kg.zukhridin.nework.presentation.listener.PostItemEventClickListener


@SuppressLint("SetTextI18n")
class PostAdapter(
    private val postItemEventClickListener: PostItemEventClickListener,
    private val activity: FragmentActivity,
    private val mediaListener: MediaListener
) :
    PagingDataAdapter<Post, PostViewHolder>(PostDiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        println("getItemViewType")
        return position
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        println("onBindViewHolder")
        val post = getItem(position)
        if (post != null) {
            holder.bind(post)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        println("onCreateViewHolder")
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, postItemEventClickListener, activity, mediaListener)
    }

}
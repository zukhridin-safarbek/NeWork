package kg.zukhridin.nework.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagingDataAdapter
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.databinding.PostItemBinding
import kg.zukhridin.nework.presentation.adapters.callback.PostDiffCallBack
import kg.zukhridin.nework.presentation.adapters.viewholders.MediaListener
import kg.zukhridin.nework.presentation.adapters.viewholders.PostViewHolder
import kg.zukhridin.nework.presentation.listener.PostItemEventClickListener


@SuppressLint("SetTextI18n")
class PostAdapter(
    private val postItemEventClickListener: PostItemEventClickListener,
    private val activity: FragmentActivity,
    private val mediaListener: MediaListener
) :
    PagingDataAdapter<Post, PostViewHolder>(PostDiffCallBack()) {
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        if (post != null) {
            holder.bind(post)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, postItemEventClickListener, activity, mediaListener)
    }

}
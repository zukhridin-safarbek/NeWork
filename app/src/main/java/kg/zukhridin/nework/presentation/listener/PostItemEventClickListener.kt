package kg.zukhridin.nework.presentation.listener

import android.view.View
import kg.zukhridin.nework.domain.models.Post

interface PostItemEventClickListener {
    fun onLike(post: Post)
    fun onMenuClick(post: Post, view: View)
    fun onMentionPeopleClick(post: Post)
    fun userDetail(userId: Int)
    fun postItemClick(post: Post)
}
package kg.zukhridin.nework.utils

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import kg.zukhridin.nework.R
import kg.zukhridin.nework.dto.Post

interface PostMenuOnItemClick {
    fun updatePost(post: Post)
    fun deletePost(post: Post)
}

class PostMenu(
    private val postMenuOnItemClick: PostMenuOnItemClick,
    private val context: Context,
    private val view: View,
    private val post: Post
) {
    fun show() {
        PopupMenu(context, view).apply {
            inflate(R.menu.post_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.editPost -> {
                        postMenuOnItemClick.updatePost(post)
                        true
                    }
                    R.id.deletePost -> {
                        postMenuOnItemClick.deletePost(post)
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }
}
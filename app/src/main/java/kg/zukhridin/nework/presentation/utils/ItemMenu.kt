package kg.zukhridin.nework.presentation.utils

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import kg.zukhridin.nework.R
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.domain.models.Post
interface PostMenuOnClick {
    fun updatePost(post: Post)
    fun deletePost(post: Post)
}

interface EventMenuOnClick {
    fun updateEvent(event: Event)
    fun deleteEvent(event: Event)
}

class ItemMenu(
    private val context: Context,
    private val view: View,
) {
    fun postMenu(post: Post, postMenuOnClick: PostMenuOnClick,) {
        PopupMenu(context, view).apply {
            inflate(R.menu.post_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.editPost -> {
                        postMenuOnClick.updatePost(post)
                        true
                    }
                    R.id.deletePost -> {
                        postMenuOnClick.deletePost(post)
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }
    fun eventMenu(event: Event, eventMenuOnClick: EventMenuOnClick) {
        PopupMenu(context, view).apply {
            inflate(R.menu.post_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.editPost -> {
                        eventMenuOnClick.updateEvent(event)
                        true
                    }
                    R.id.deletePost -> {
                        eventMenuOnClick.deleteEvent(event)
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }
}
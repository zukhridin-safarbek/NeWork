package kg.zukhridin.nework.presentation.listener

import android.view.View
import kg.zukhridin.nework.domain.models.Event

interface EventItemClickListener {
    fun onLike(event: Event)
    fun onMenuClick(event: Event, view: View)
}
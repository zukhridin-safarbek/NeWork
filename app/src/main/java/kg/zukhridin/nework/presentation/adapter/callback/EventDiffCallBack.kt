package kg.zukhridin.nework.presentation.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import kg.zukhridin.nework.domain.models.Event

class EventDiffCallBack : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: Event, newItem: Event): Any {
        return Unit
    }
}
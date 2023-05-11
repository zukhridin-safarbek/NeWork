package kg.zukhridin.nework.callback

import androidx.recyclerview.widget.DiffUtil
import kg.zukhridin.nework.dto.EventDto

class EventDiffCallBack : DiffUtil.ItemCallback<EventDto>() {
    override fun areItemsTheSame(oldItem: EventDto, newItem: EventDto): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: EventDto, newItem: EventDto): Boolean = oldItem == newItem
}
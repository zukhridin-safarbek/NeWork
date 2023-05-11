package kg.zukhridin.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import kg.zukhridin.nework.adapter.viewholders.EventViewHolder
import kg.zukhridin.nework.callback.EventDiffCallBack
import kg.zukhridin.nework.databinding.EventItemBinding
import kg.zukhridin.nework.dto.EventDto

class EventAdapter : PagingDataAdapter<EventDto, EventViewHolder>(EventDiffCallBack()) {
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        if (event != null) {
            holder.bind(event)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return (EventViewHolder(binding))
    }
}
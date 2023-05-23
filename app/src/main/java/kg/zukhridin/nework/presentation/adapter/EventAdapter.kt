package kg.zukhridin.nework.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import kg.zukhridin.nework.databinding.EventItemBinding
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.presentation.adapter.callback.EventDiffCallBack
import kg.zukhridin.nework.presentation.adapter.viewholders.EventViewHolder
import kg.zukhridin.nework.presentation.listener.EventItemClickListener

class EventAdapter(private val eventItemClickListener: EventItemClickListener) :
    PagingDataAdapter<Event, EventViewHolder>(EventDiffCallBack()) {
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)

        if (event != null) {
            holder.bind(event)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return (EventViewHolder(binding, eventItemClickListener))
    }
}
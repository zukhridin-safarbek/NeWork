package kg.zukhridin.nework.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagingDataAdapter
import kg.zukhridin.nework.databinding.EventItemBinding
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.presentation.adapters.callback.EventDiffCallBack
import kg.zukhridin.nework.presentation.adapters.viewholders.EventViewHolder
import kg.zukhridin.nework.presentation.adapters.viewholders.MediaListener
import kg.zukhridin.nework.presentation.listener.EventItemClickListener

class EventAdapter(private val eventItemClickListener: EventItemClickListener, private val mediaListener: MediaListener, private val activity: FragmentActivity) :
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

        return (EventViewHolder(binding, eventItemClickListener, mediaListener, activity))
    }
}
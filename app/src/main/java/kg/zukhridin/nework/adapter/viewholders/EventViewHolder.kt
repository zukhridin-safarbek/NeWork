package kg.zukhridin.nework.adapter.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.zukhridin.nework.databinding.EventItemBinding
import kg.zukhridin.nework.dto.EventDto

class EventViewHolder(private val binding: EventItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(eventDto: EventDto) {
        Glide.with(binding.eventImage).load(eventDto.attachment?.url).circleCrop()
            .into(binding.eventImage)
    }
}
package kg.zukhridin.nework.adapter.viewholders

import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.adapter.MentionPeopleItemListener
import kg.zukhridin.nework.databinding.MentionedItemsBinding

class MentionPeopleViewHolder(private val binding: MentionedItemsBinding, private val listener: MentionPeopleItemListener) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(text: String) {
        binding.mention.text = text
        binding.mention.setOnClickListener {
            listener.itemClick(text)
        }

    }
}
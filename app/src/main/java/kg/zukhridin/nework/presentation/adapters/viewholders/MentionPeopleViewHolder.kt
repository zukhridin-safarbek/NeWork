package kg.zukhridin.nework.presentation.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.presentation.adapters.MentionPeopleItemListener
import kg.zukhridin.nework.databinding.MentionedItemsBinding

class MentionPeopleViewHolder(private val binding: MentionedItemsBinding, private val mentionPeopleItemListener: MentionPeopleItemListener) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(text: String) {
        binding.mention.text = text
        binding.mention.setOnClickListener {
            mentionPeopleItemListener.removeItem(text)
        }

    }
}
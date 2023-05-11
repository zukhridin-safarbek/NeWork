package kg.zukhridin.nework.adapter.viewholders

import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.databinding.MentionedPeopleItemListBinding
interface MentionItemClickListener{
    fun mentionItemClick(text: String)
}
class MentionPeopleItemShowViewHolder(private val binding: MentionedPeopleItemListBinding, private val mentionItemClickListener: MentionItemClickListener) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(text: String) {
        binding.mentionPeopleBtn.text = text
        binding.mentionPeopleBtn.setOnClickListener {
            mentionItemClickListener.mentionItemClick(text)
        }
    }
}
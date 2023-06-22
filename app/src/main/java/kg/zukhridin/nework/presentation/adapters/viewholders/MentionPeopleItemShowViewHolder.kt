package kg.zukhridin.nework.presentation.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.databinding.MentionedPeopleItemListBinding
import kg.zukhridin.nework.domain.models.User

interface MentionItemClickListener{
    fun mentionItemClick(user: User)
}
class MentionPeopleItemShowViewHolder(private val binding: MentionedPeopleItemListBinding, private val mentionItemClickListener: MentionItemClickListener) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User?) {
        if (user != null){
            binding.mentionPeopleBtn.text = user.login
            binding.mentionPeopleBtn.setOnClickListener {
                mentionItemClickListener.mentionItemClick(user)
            }
        }
    }
}
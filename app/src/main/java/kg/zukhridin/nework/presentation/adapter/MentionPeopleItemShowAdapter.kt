package kg.zukhridin.nework.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.databinding.MentionedPeopleItemListBinding
import kg.zukhridin.nework.presentation.adapter.viewholders.MentionItemClickListener
import kg.zukhridin.nework.presentation.adapter.viewholders.MentionPeopleItemShowViewHolder

class MentionPeopleItemShowAdapter(private val list: List<String>, private val mentionItemClickListener: MentionItemClickListener) :
    RecyclerView.Adapter<MentionPeopleItemShowViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MentionPeopleItemShowViewHolder {
        val binding =
            MentionedPeopleItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MentionPeopleItemShowViewHolder(binding, mentionItemClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MentionPeopleItemShowViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }
}
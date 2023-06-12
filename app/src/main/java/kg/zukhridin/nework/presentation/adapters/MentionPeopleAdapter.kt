package kg.zukhridin.nework.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.databinding.MentionedItemsBinding
import kg.zukhridin.nework.presentation.adapters.viewholders.MentionPeopleViewHolder

interface MentionPeopleItemListener {
    fun itemClick(mentioned: String)
}

class MentionPeopleAdapter(
    private val list: List<String>,
    private val listener: MentionPeopleItemListener
) :
    RecyclerView.Adapter<MentionPeopleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentionPeopleViewHolder {
        val binding =
            MentionedItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MentionPeopleViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MentionPeopleViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }
}
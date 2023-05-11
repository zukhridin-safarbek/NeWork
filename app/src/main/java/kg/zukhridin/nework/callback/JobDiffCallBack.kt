package kg.zukhridin.nework.callback

import androidx.recyclerview.widget.DiffUtil
import kg.zukhridin.nework.dto.Job

class JobDiffCallBack : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: Job, newItem: Job): Any {
        return Unit
    }
}

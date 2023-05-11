package kg.zukhridin.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kg.zukhridin.nework.adapter.viewholders.JobViewHolder
import kg.zukhridin.nework.callback.JobDiffCallBack
import kg.zukhridin.nework.databinding.JobItemBinding
import kg.zukhridin.nework.dto.Job

class JobAdapter : ListAdapter<Job, JobViewHolder>(JobDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = JobItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }
}
package kg.zukhridin.nework.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.R
import kg.zukhridin.nework.databinding.JobItemBinding
import kg.zukhridin.nework.dto.Job
import kg.zukhridin.nework.utils.CustomOffsetDateTime.Companion.timeDecode

class JobViewHolder(private val binding: JobItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(job: Job) = with(binding) {
        jobName.text = job.name
        jobPosition.text = job.position
        if (job.link != null) {
            jobLink.text = job.link
        } else {
            jobLink.visibility = View.GONE
        }
        jobStart.text =
            "${binding.root.context.getString(R.string.start)}: ${timeDecode(job.start)}"
        if (job.finish != null) {
            jobFinish.text = "${binding.root.context.getString(R.string.finish)}: ${timeDecode(job.finish)}"
        } else {
            jobFinish.visibility = View.GONE
        }
    }
}
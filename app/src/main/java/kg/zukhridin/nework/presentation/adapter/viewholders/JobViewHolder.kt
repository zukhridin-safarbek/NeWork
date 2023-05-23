package kg.zukhridin.nework.presentation.adapter.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.R
import kg.zukhridin.nework.domain.models.Job
import kg.zukhridin.nework.databinding.JobItemBinding
import kg.zukhridin.nework.presentation.utils.CustomOffsetDateTime.Companion.timeDecode

@SuppressLint("SetTextI18n")
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
            jobFinish.text =
                "${binding.root.context.getString(R.string.finish)}: ${timeDecode(job.finish!!)}"
        } else {
            jobFinish.visibility = View.GONE
        }
    }
}
package kg.zukhridin.nework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.databinding.FragmentJobBinding
import kg.zukhridin.nework.dto.Job
import kg.zukhridin.nework.viewmodel.JobViewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class JobFragment : Fragment() {
    private lateinit var binding: FragmentJobBinding
    private val jobVM: JobViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            if (jobVM.getMyJobs().first) {
                jobVM.getMyJobs().second.collectLatest {
                    println("job: $it")
                }
            }
            binding.btn.setOnClickListener {
                lifecycleScope.launchWhenCreated {
                    val job = Job(0, "OSHSU", "Web developer", "2023-04-24T13:19:31.428000Z")
                    jobVM.createJob(job)
                }
            }
        }
    }
}
package kg.zukhridin.nework.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.databinding.FragmentAddJobBinding
import kg.zukhridin.nework.dto.Job
import kg.zukhridin.nework.viewmodel.JobViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Calendar

@AndroidEntryPoint
class AddJobFragment : Fragment() {
    private lateinit var binding: FragmentAddJobBinding
    private val jobVM: JobViewModel by viewModels()
    private lateinit var jobStartDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var jobFinishDateListener: DatePickerDialog.OnDateSetListener
    private var jobStartDateTime = ""
    private var jobFinishDateTime = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.add.setOnClickListener {
            val job = Job(
                0,
                binding.jobName.text.toString(),
                binding.jobPosition.text.toString(),
                jobStartDateTime,
                jobFinishDateTime.ifBlank { null },
                null
            )
            lifecycleScope.launchWhenCreated {
                val success = jobVM.createJob(job)
                if (success) {
                    findNavController().navigateUp()
                }
            }
        }
        binding.jobStart.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth,
                jobStartDateListener,
                year, month, day
            )
            dialog.show()
        }
        binding.jobFinish.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth,
                jobFinishDateListener,
                year, month, day
            )
            dialog.show()
        }
        jobStartDateListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val date =
                    LocalDateTime.of(LocalDate.of(year, month + 1, dayOfMonth), LocalTime.now())
                jobStartDateTime = OffsetDateTime.of(date, ZoneOffset.UTC).toString()
                binding.jobStart.text = "$dayOfMonth/$month/$year"
            }
        jobFinishDateListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val date =
                    LocalDateTime.of(LocalDate.of(year, month + 1, dayOfMonth), LocalTime.now())
                jobFinishDateTime = OffsetDateTime.of(date, ZoneOffset.UTC).toString()
                binding.jobFinish.text = "$dayOfMonth/$month/$year"
            }
    }
}
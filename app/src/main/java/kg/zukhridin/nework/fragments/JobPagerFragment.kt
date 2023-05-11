package kg.zukhridin.nework.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.adapter.JobAdapter
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentJobPagerBinding
import kg.zukhridin.nework.dto.Job
import kg.zukhridin.nework.utils.AppPrefs
import kg.zukhridin.nework.utils.USER
import kg.zukhridin.nework.utils.USER_ID
import kg.zukhridin.nework.viewmodel.JobViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class JobPagerFragment : Fragment() {
    private lateinit var binding: FragmentJobPagerBinding
    private val jobVM: JobViewModel by viewModels()
    private lateinit var jobAdapter: JobAdapter
    private lateinit var ref: SharedPreferences
    private val jobsHere: MutableList<Job> = mutableListOf()

    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var appPrefs: AppPrefs
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobPagerBinding.inflate(inflater, container, false)
        jobAdapter = JobAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcView.adapter = jobAdapter
        lifecycleScope.launchWhenCreated {
            getJobs()
        }

    }

    private suspend fun getJobs() {
        ref = requireActivity().getSharedPreferences(USER, Context.MODE_PRIVATE)
        val userId =
            appPrefs.postItemCLickStateFlow.value?.userId ?: appAuth.authStateFlow.value?.id
        if (userId == 0) {
            deleteJob()
        }
        if ((if (userId == 0) appAuth.authStateFlow.value?.id ?: 0 else userId)?.let {
                jobVM.getUserJobs(
                    it
                ).first
            } == true
        ) {
            (if (userId == 0) appAuth.authStateFlow.value?.id ?: 0 else userId)?.let {
                jobVM.getUserJobs(
                    it
                ).second.collectLatest {
                    if (it.isEmpty()) {
                        binding.rcView.visibility = View.GONE
                        binding.emptyText.visibility = View.VISIBLE
                    } else {
                        binding.rcView.visibility = View.VISIBLE
                        binding.emptyText.visibility = View.GONE
                        it.map { job ->
                            jobsHere.add(job)
                        }
                        jobAdapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun deleteJob() {
        val swipeGestureRecyclerView =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                val deleteDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete)
                val intrinsicWidth = deleteDrawable?.intrinsicWidth;
                val intrinsicHeight = deleteDrawable?.intrinsicHeight;
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.absoluteAdapterPosition
                    lifecycleScope.launchWhenCreated {
                        println(jobsHere[position])
                        jobVM.deleteMyJob(jobsHere[position])
                        jobsHere.remove(jobsHere[position])
                    }

                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    if (
                        actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                    ) {
                        if (dX < 0) {
                            //draw background
                            val itemView = viewHolder.itemView
                            val bg: ColorDrawable = ColorDrawable()
                            bg.color = Color.RED
                            bg.setBounds(
                                itemView.left,
                                itemView.top,
                                itemView.right,
                                itemView.bottom
                            )
                            bg.draw(c)
                            //draw icon
                            val itemHeight = itemView.height
                            val deleteIconTop: Int =
                                itemView.top + (itemHeight - intrinsicHeight!!) / 2
                            val deleteIconMargin: Int = (itemHeight - intrinsicHeight) / 2
                            val deleteIconLeft: Int =
                                itemView.right - deleteIconMargin - intrinsicWidth!!
                            val deleteIconRight = itemView.right - deleteIconMargin
                            val deleteIconBottom: Int = deleteIconTop + intrinsicHeight

                            deleteDrawable.apply {
                                this?.let {
                                    DrawableCompat.wrap(it).apply {
                                        setTint(Color.WHITE)
                                        setTintMode(PorterDuff.Mode.SRC_IN)
                                    }
                                }
                            }
                            deleteDrawable?.setBounds(
                                deleteIconLeft,
                                deleteIconTop,
                                deleteIconRight,
                                deleteIconBottom
                            )
                            deleteDrawable?.draw(c)
                        }
                    }
                }

            }
        ItemTouchHelper(swipeGestureRecyclerView).attachToRecyclerView(binding.rcView)
    }

}
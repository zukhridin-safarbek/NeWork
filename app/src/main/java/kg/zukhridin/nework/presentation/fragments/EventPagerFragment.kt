package kg.zukhridin.nework.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.SimpleItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.data.entity.EventEntity
import kg.zukhridin.nework.presentation.adapter.EventAdapter
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.data.storage.dao.PostDao
import kg.zukhridin.nework.databinding.FragmentEventPagerBinding
import kg.zukhridin.nework.presentation.decorations.EventDividerItemDecoration
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.presentation.listener.EventItemClickListener
import kg.zukhridin.nework.data.util.AppPrefs
import kg.zukhridin.nework.presentation.utils.CheckNetwork
import kg.zukhridin.nework.presentation.utils.EventMenuOnClick
import kg.zukhridin.nework.presentation.utils.ItemMenu
import kg.zukhridin.nework.presentation.viewmodel.EventViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class EventPagerFragment : Fragment(), EventItemClickListener, EventMenuOnClick {
    private lateinit var binding: FragmentEventPagerBinding

    @Inject
    lateinit var appPrefs: AppPrefs

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var postDao: PostDao

    @Inject
    lateinit var checkNetwork: CheckNetwork
    private val eventVM: EventViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventPagerBinding.inflate(inflater, container, false)
        eventAdapter = EventAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcViewEventsControl()
        lifecycleScope.launchWhenCreated {
            eventAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible = it.refresh is LoadState.Loading
            }
        }
    }

    private fun rcViewEventsControl() = with(binding) {
        lifecycleScope.launchWhenCreated {
            eventVM.clearAllEvents()
            eventVM.data.collectLatest { pagingData ->
                rcView.apply {
                    adapter = eventAdapter
                    (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                    eventAdapter.submitData(pagingData = pagingData)
                    addItemDecoration(EventDividerItemDecoration(10))
                }
            }
        }
    }

    override fun onLike(event: Event) {
        eventVM.eventLikeById(event)
    }

    override fun onMenuClick(event: Event, view: View) {
        ItemMenu(requireContext(), view).eventMenu(event, this)
    }

    override fun updateEvent(event: Event) {
        TODO("Update event")
    }

    override fun deleteEvent(event: Event) {
        lifecycleScope.launchWhenStarted { eventVM.deleteEvent(event) }
    }
}
package kg.zukhridin.nework.presentation.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.data.storage.dao.PostDao
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.data.util.AppPrefs
import kg.zukhridin.nework.databinding.FragmentEventPagerBinding
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.presentation.adapters.EventAdapter
import kg.zukhridin.nework.presentation.adapters.viewholders.MediaListener
import kg.zukhridin.nework.presentation.listener.EventItemClickListener
import kg.zukhridin.nework.presentation.utils.CheckNetwork
import kg.zukhridin.nework.presentation.utils.EventMenuOnClick
import kg.zukhridin.nework.presentation.utils.ItemMenu
import kg.zukhridin.nework.presentation.viewmodel.EventViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EventPagerFragment : Fragment(), EventItemClickListener, EventMenuOnClick, MediaListener {
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
        eventAdapter = EventAdapter(this, this, requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcViewEventsControl()
        lifecycleScope.launchWhenCreated {
            onSwipeRefresh()
            eventAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible =
                    it.refresh is LoadState.Loading && !binding.swiperRefreshLayout.isRefreshing
            }
        }
    }

    private suspend fun onSwipeRefresh() {
        binding.swiperRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                eventAdapter.refresh()
                eventAdapter.loadStateFlow.collectLatest {
                    binding.swiperRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
                }
            }
        }
    }

    private fun rcViewEventsControl() = with(binding) {
        lifecycleScope.launchWhenCreated {
            rcView.apply {
                adapter = eventAdapter
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                eventVM.data.collectLatest { pagingData ->
                    eventAdapter.submitData(pagingData = pagingData)
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

    override fun play(value: Any) {
        val event = value as Event
        if (event.attachment?.type == kg.zukhridin.nework.domain.enums.AttachmentType.VIDEO) {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.layout_bottom_sheet_video_player)
            val videoView = dialog.findViewById<PlayerView>(R.id.videoView)
            val close = dialog.findViewById<MaterialButton>(R.id.closeDialog)
            close.setOnClickListener {
                dialog.dismiss()
                player.stop()
            }
            if (!dialog.isShowing) {
                player.stop()
            }
            dialog.setCancelable(false)
            videoView.player = player
            videoView.useController = true
            val mediaItem = MediaItem.fromUri(event.attachment!!.url)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setGravity(Gravity.BOTTOM)
            if (checkNetwork.networkAvailable()) {
                dialog.show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.you_are_have_not_internet),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (event.attachment?.type == kg.zukhridin.nework.domain.enums.AttachmentType.AUDIO) {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.layout_bottom_sheet_audio_player)
            val audioView = dialog.findViewById<PlayerView>(R.id.audioView)
            val close = dialog.findViewById<MaterialButton>(R.id.closeDialog)
            close.setOnClickListener {
                dialog.dismiss()
                player.stop()
            }
            if (!dialog.isShowing) {
                player.stop()
            }
            dialog.setCancelable(false)
            audioView.player = player
            audioView.useController = true
            val mediaItem = MediaItem.fromUri(event.attachment!!.url)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setGravity(Gravity.BOTTOM)
            if (checkNetwork.networkAvailable()) {
                dialog.show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.you_are_have_not_internet),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
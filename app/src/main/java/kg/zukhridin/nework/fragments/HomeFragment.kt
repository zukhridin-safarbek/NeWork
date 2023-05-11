package kg.zukhridin.nework.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.adapter.*
import kg.zukhridin.nework.adapter.viewholders.MentionItemClickListener
import kg.zukhridin.nework.adapter.viewholders.VideoListener
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.databinding.FragmentHomeBinding
import kg.zukhridin.nework.decorations.PostDividerItemDecoration
import kg.zukhridin.nework.dto.AttachmentType
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.entity.EventEntity
import kg.zukhridin.nework.utils.*
import kg.zukhridin.nework.viewmodel.EventViewModel
import kg.zukhridin.nework.viewmodel.PostViewModel
import kg.zukhridin.nework.viewmodel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HomeFragment : Fragment(), PostOnItemEventsListener, VideoListener, PostMenuOnItemClick,
    MentionItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val postVM: PostViewModel by viewModels()
    private val userVM: UserViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter
    private val users = mutableListOf<User>()

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

    private lateinit var timer: Timer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        timer = Timer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postAdapter = PostAdapter(this, requireActivity(), player, this)
        eventAdapter = EventAdapter()
        if (checkNetwork.networkAvailable()) {
            binding.rcView.visibility = View.VISIBLE
            binding.internetWarning.visibility = View.GONE
            rcViewEventsControl()
            rcViewControl()
        } else {
            binding.rcView.visibility = View.GONE
            binding.internetWarning.visibility = View.VISIBLE
            checkNetworkEveryThreeSecond()
        }
    }

    private fun checkNetworkEveryThreeSecond() {
        val task = object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    if (checkNetwork.networkAvailable()) {
                        binding.rcView.visibility = View.VISIBLE
                        binding.internetWarning.visibility = View.GONE
                        rcViewControl()
                        rcViewEventsControl()
                        timer.cancel()
                        timer.purge()
                    } else {
                        binding.rcView.visibility = View.GONE
                        binding.internetWarning.visibility = View.VISIBLE
                    }
                }
            }
        }
        if (!checkNetwork.networkAvailable()) {
            timer.schedule(task, 0, 1000)
        } else {
            timer.cancel()
            timer.purge()
        }
    }

    private fun rcViewEventsControl() = with(binding) {
        lifecycleScope.launchWhenCreated {
            eventVM.data.collectLatest {
                rcViewHistory.adapter = eventAdapter
                eventAdapter.submitData(pagingData = it)
            }
        }
    }

    private fun rcViewControl() = with(binding) {
        getUsers()
        swipeRefresh.setOnRefreshListener {
            postAdapter.refresh()
        }
        rcView.apply {
            scrollToPosition(0)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                PostDividerItemDecoration(
                    binding.rcView.context,
                    PostDividerItemDecoration.VERTICAL,
                    24
                )
            )
            adapter = postAdapter.withLoadStateHeaderAndFooter(
                PostsLoaderStateAdapter(requireContext()),
                PostsLoaderStateAdapter(requireContext())
            )

        }
        lifecycleScope.launchWhenCreated {
            postVM.data.collectLatest { pagingData ->
                postAdapter.submitData(pagingData)
            }
        }
        lifecycleScope.launchWhenCreated {
            postAdapter.loadStateFlow.collectLatest {
                swipeRefresh.isRefreshing = it.refresh is LoadState.Loading
            }
        }
    }

    private fun getUsers() {
        userVM.users.observe(viewLifecycleOwner) { userList ->
            userList?.map { user ->
                users.add(user)
            }
        }
    }

    override fun onLike(post: Post) {
        if (checkNetwork.networkAvailable()) {
            postVM.likeById(post)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.you_are_have_not_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onMenuClick(post: Post, view: View) {
        PostMenu(this, requireContext(), view, post).show()
    }

    override fun onMentionPeopleClick(post: Post) {
        val dialog = BottomSheetDialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.bottom_sheet_mention_people)
            window?.setGravity(Gravity.BOTTOM)
            window?.setWindowAnimations(R.style.DialogAnimation)
            window?.setBackgroundDrawable(ColorDrawable(Color.argb(25, 0, 0, 0)))
        }
        val rcView = dialog.findViewById<RecyclerView>(R.id.rcView)
        val usersLogin = mutableListOf<String>()
        users.forEach { user ->
            post.mentionIds.forEach { mentionId ->
                if (user.id == mentionId) {
                    usersLogin.add(user.login)
                }
            }
        }
        val mpAdapter = MentionPeopleItemShowAdapter(usersLogin.distinct(), this)
        rcView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        rcView?.adapter = mpAdapter
        dialog.show()

    }

    override fun userDetail(userId: Int) {
        println("userId: $userId")
        appPrefs.setPostClickUserId(userId)
        findNavController().navigate(
            R.id.action_homeFragment_to_userDetailFragment
        )
    }


    override fun postItemClick(post: Post) {
        Unit
    }


    override fun start(post: Post) {
        println(post)
        if (post.attachment?.type == AttachmentType.VIDEO) {
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
            val mediaItem = MediaItem.fromUri(post.attachment.url)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            dialog.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
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
        } else if (post.attachment?.type == AttachmentType.AUDIO) {
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
            val mediaItem = MediaItem.fromUri(post.attachment.url)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            dialog.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
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

    override fun updatePost(post: Post) {
        if (checkNetwork.networkAvailable()) {
            findNavController().navigate(
                R.id.action_homeFragment_to_editPostFragment,
                Bundle().apply {
                    editedPostId = post.id.toString()
                })
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.you_are_have_not_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    override fun onStop() {
        super.onStop()
        player.stop()
        timer.cancel()
        timer.purge()
    }

    override fun onDetach() {
        super.onDetach()
        player.stop()
    }

    override fun deletePost(post: Post) {
        postVM.deleteById(post.id)
        postVM.deletePostByIdFromExternalStorage(post.id)
    }

    override fun mentionItemClick(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        var Bundle.userId: String? by DataTransferArg
        var Bundle.editedPostId: String? by DataTransferArg
    }

}
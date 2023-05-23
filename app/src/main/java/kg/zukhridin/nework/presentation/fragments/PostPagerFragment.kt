package kg.zukhridin.nework.presentation.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.data.storage.dao.PostDao
import kg.zukhridin.nework.databinding.FragmentPostPagerBinding
import kg.zukhridin.nework.presentation.decorations.PostDividerItemDecoration
import kg.zukhridin.nework.presentation.adapter.MentionPeopleItemShowAdapter
import kg.zukhridin.nework.presentation.adapter.PostAdapter
import kg.zukhridin.nework.presentation.adapter.PostsLoaderStateAdapter
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.domain.models.User
import kg.zukhridin.nework.presentation.listener.PostItemEventClickListener
import kg.zukhridin.nework.data.util.AppPrefs
import kg.zukhridin.nework.domain.enums.AttachmentType
import kg.zukhridin.nework.presentation.adapter.viewholders.MediaListener
import kg.zukhridin.nework.presentation.adapter.viewholders.MentionItemClickListener
import kg.zukhridin.nework.presentation.utils.CheckNetwork
import kg.zukhridin.nework.presentation.utils.ItemMenu
import kg.zukhridin.nework.presentation.utils.PostMenuOnClick
import kg.zukhridin.nework.presentation.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class PostPagerFragment : Fragment(), PostItemEventClickListener, MediaListener,
    PostMenuOnClick,
    MentionItemClickListener {
    private lateinit var binding: FragmentPostPagerBinding
    private val postVM: PostViewModel by viewModels()
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostPagerBinding.inflate(inflater, container, false)
        postAdapter =
            PostAdapter(this, requireActivity(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated { postVM.clearAllPosts() }
        rcViewControl()
        lifecycleScope.launchWhenCreated {
            postAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible = it.refresh is LoadState.Loading
            }
        }
    }

    private fun rcViewControl() = with(binding) {
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
                PostsLoaderStateAdapter(
                    requireContext()
                ),
                PostsLoaderStateAdapter(
                    requireContext()
                )
            )

        }
        lifecycleScope.launchWhenCreated {
            postVM.data.collectLatest { pagingData ->
                postAdapter.submitData(pagingData)
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
        ItemMenu(requireContext(), view).postMenu(post, this)
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
        val mpAdapter = MentionPeopleItemShowAdapter(
            usersLogin.distinct(),
            this
        )
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
    }

    override fun play(post: Post) {
        if (post.attachment?.type == kg.zukhridin.nework.domain.enums.AttachmentType.VIDEO) {
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
            val mediaItem = MediaItem.fromUri(post.attachment!!.url)
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
        } else if (post.attachment?.type == kg.zukhridin.nework.domain.enums.AttachmentType.AUDIO) {
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
            val mediaItem = MediaItem.fromUri(post.attachment!!.url)
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

    override fun updatePost(post: Post) {
        if (checkNetwork.networkAvailable()) {
            appPrefs.setPostClickPostId(post.id)
            findNavController().navigate(
                R.id.action_homeFragment_to_editPostFragment
            )
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.you_are_have_not_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun deletePost(post: Post) {
        postVM.deleteById(post.id)
    }

    override fun mentionItemClick(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }
}
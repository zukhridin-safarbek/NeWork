package kg.zukhridin.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.databinding.FragmentPostDetailWithAudioBinding
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.data.util.AppPrefs
import kg.zukhridin.nework.data.util.Constants.PROFILE_FRAGMENT
import kg.zukhridin.nework.data.util.Constants.USER_DETAIL_FRAGMENT
import kg.zukhridin.nework.domain.enums.AttachmentType
import kg.zukhridin.nework.presentation.utils.*
import kg.zukhridin.nework.presentation.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailFragmentWithAudio : Fragment(), PostMenuOnClick {
    @Inject
    lateinit var appPrefs: AppPrefs

    @Inject
    lateinit var checkNetwork: CheckNetwork
    private val postVM: PostViewModel by viewModels()
    private lateinit var binding: FragmentPostDetailWithAudioBinding
    private lateinit var player: CustomMediaPlayer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailWithAudioBinding.inflate(inflater, container, false)
        player = CustomMediaPlayer(requireContext(), binding.contentAudio, kg.zukhridin.nework.domain.enums.AttachmentType.AUDIO)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postId = appPrefs.postItemClickStateFlow.value?.postId
        if (postId != 0) {
            lifecycleScope.launchWhenCreated {
                if (postId != null) {
                    val post = postVM.getPostById(postId)
                    binding.author.text = post.author
                    CoordinationControl.postCoordinationControl(post, binding.coordination)
                    binding.contentText.text =
                        post.content
                    post.attachment?.url?.let { player.download(it) }

                    player.setController(true)
                    player.play()
                    binding.likeTv.text =
                        post.likeOwnerIds.size.toString()
                    binding.itemSettings.isVisible = post.ownedByMe
                    binding.itemSettings.setOnClickListener {
                        ItemMenu(
                            requireContext(),
                            binding.itemSettings
                        ).postMenu(post, this@PostDetailFragmentWithAudio)
                    }
                    showFullTextMoreBtn(binding.btnMore, binding.contentText, post.content)
                    avatarControl(
                        binding.authorAvatar,
                        post.authorAvatar
                            ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg"
                    )
                    binding.mentionPeople.isVisible = post.mentionIds.isNotEmpty()
                    binding.mentionPeople.setOnClickListener {
                        TODO("listener.onMentionPeopleClick(post)")
                    }
                    if (!post.link.isNullOrBlank()) {
                        binding.link.visibility = View.VISIBLE
                        binding.link.text = post.link
                    } else {
                        binding.link.visibility = View.GONE
                    }
                    binding.published.text = post.published
                    setVisibility()
                }
            }
        }
    }

    private fun setVisibility() {
        binding.progressBar.visibility = View.GONE
        binding.bodyScrollView.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }

    override fun updatePost(post: Post) {
        if (checkNetwork.networkAvailable()) {
            findNavController().navigate(
                R.id.action_homeFragment_to_editPostFragment
            )
            appPrefs.setPostClickPostId(post.id)
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
        val fragmentName = appPrefs.getFragmentName()
        if (fragmentName != null) {
            when (fragmentName) {
                USER_DETAIL_FRAGMENT -> {
                    findNavController().navigate(R.id.action_postDetailFragmentWithAudio_to_userDetailFragment)
                }
                PROFILE_FRAGMENT -> {
                    findNavController().navigate(R.id.action_postDetailFragmentWithAudio_to_profileFragment)
                }
            }
        }

    }
}
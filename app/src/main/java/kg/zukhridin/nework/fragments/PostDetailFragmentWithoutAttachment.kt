package kg.zukhridin.nework.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.adapter.downloadAvatar
import kg.zukhridin.nework.adapter.viewholders.showFullTextMoreBtn
import kg.zukhridin.nework.custom.CoordinationControl
import kg.zukhridin.nework.databinding.FragmentPostDetailWithoutAttachmentBinding
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.fragments.HomeFragment.Companion.editedPostId
import kg.zukhridin.nework.utils.*
import kg.zukhridin.nework.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailFragmentWithoutAttachment : Fragment(), PostMenuOnItemClick {
    private lateinit var binding: FragmentPostDetailWithoutAttachmentBinding
    @Inject
    lateinit var appPrefs: AppPrefs
    @Inject
    lateinit var checkNetwork: CheckNetwork
    private val postVM: PostViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailWithoutAttachmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postId = appPrefs.postItemCLickStateFlow.value?.postId
        println("postid: $postId")
        if (postId != 0) {
            lifecycleScope.launchWhenCreated {
                if (postId != null) {
                    val post = postVM.getPostById(postId)
                    binding.author.text = post.author
                    CoordinationControl.coordinationControl(post, binding.coordination)
                    binding.contentText.text = post.content
                    binding.likeCount.text =
                        post.likeOwnerIds.size.toString()
                    binding.itemSettings.isVisible = post.ownedByMe
                    binding.itemSettings.setOnClickListener {
                        PostMenu(this@PostDetailFragmentWithoutAttachment, requireContext(), it, post).show()
                    }
                    binding.mentionPeople.isVisible = post.mentionIds.isNotEmpty()
                    binding.mentionPeople.setOnClickListener {

                    }
                    downloadAvatar(
                        binding.authorAvatar,
                        post.authorAvatar
                            ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg"
                    )
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
    private fun setVisibility(){
        binding.progressBar.visibility = View.GONE
        binding.bodyScrollView.visibility = View.VISIBLE
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

    override fun deletePost(post: Post) {
        postVM.deleteById(post.id)
        postVM.deletePostByIdFromExternalStorage(post.id)
        val fragmentName = appPrefs.getFragmentName()
        if (fragmentName != null) {
            when (fragmentName) {
                USER_DETAIL_FRAGMENT -> {
                    findNavController().navigate(R.id.action_postDetailFragmentWithoutAttachment_to_userDetailFragment)
                }
                PROFILE_FRAGMENT -> {
                    findNavController().navigate(R.id.action_postDetailFragmentWithoutAttachment_to_profileFragment)
                }
            }
        }

    }
}
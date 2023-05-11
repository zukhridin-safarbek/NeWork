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
import kg.zukhridin.nework.custom.CoordinationControl
import kg.zukhridin.nework.databinding.FragmentPostDetailBinding
import kg.zukhridin.nework.databinding.PostDetailWithImageBinding
import kg.zukhridin.nework.databinding.PostItemWithImageBinding
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.fragments.HomeFragment.Companion.editedPostId
import kg.zukhridin.nework.utils.*
import kg.zukhridin.nework.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.internal.wait
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailFragmentWithImage : Fragment(), PostMenuOnItemClick {
    private lateinit var binding: PostDetailWithImageBinding

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
        binding = PostDetailWithImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated { control() }
    }

    private suspend fun control() {
        initial()

    }
    private fun setVisibility(){
        binding.progressBar.visibility = View.GONE
        binding.bodyScrollView.visibility = View.VISIBLE
    }

    private suspend fun initial() {
        val postId = appPrefs.postItemCLickStateFlow.value?.postId
        if (postId != 0 && postId != null) {
            lifecycleScope.launchWhenCreated {
                val post = postVM.getPostById(postId)
                Glide.with(binding.contentImage).load(post.attachment?.url).fitCenter()
                    .into(binding.contentImage)
                binding.contentText.text = post.content
                binding.likeCount.text = post.likeOwnerIds.size.toString()
                binding.author.text = post.author
                Glide.with(binding.authorAvatar).load(
                    post.authorAvatar
                        ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg"
                ).circleCrop()
                    .into(binding.authorAvatar)
                CoordinationControl.coordinationControl(post, binding.coordination)
                binding.itemSettings.isVisible = post.ownedByMe
                binding.itemSettings.setOnClickListener {
                    PostMenu(this@PostDetailFragmentWithImage, requireContext(), it, post).show()
                }
                binding.link.text = post.link
                binding.btnMore.visibility = View.GONE
                binding.mentionPeople.isVisible = post.mentionIds.isNotEmpty()
                setVisibility()
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

    override fun deletePost(post: Post) {
        postVM.deleteById(post.id)
        postVM.deletePostByIdFromExternalStorage(post.id)
        val fragmentName = appPrefs.getFragmentName()
        if (fragmentName != null) {
            when (fragmentName) {
                USER_DETAIL_FRAGMENT -> {
                    findNavController().navigate(R.id.action_postDetailWithImage_to_userDetailFragment)
                }
                PROFILE_FRAGMENT -> {
                    findNavController().navigate(R.id.action_postDetailWithImage_to_profileFragment)
                }
            }
        }
    }
}
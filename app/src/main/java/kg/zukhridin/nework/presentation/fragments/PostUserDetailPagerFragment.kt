package kg.zukhridin.nework.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentPostUserDetailPagerBinding
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.data.util.AppPrefs
import kg.zukhridin.nework.presentation.adapters.UserWallAdapter
import kg.zukhridin.nework.presentation.adapters.viewholders.PostItemClickListener
import kg.zukhridin.nework.presentation.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class PostUserDetailPagerFragment : Fragment(), PostItemClickListener {
    private lateinit var binding: FragmentPostUserDetailPagerBinding
    private val postVM: PostViewModel by viewModels()

    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var appPrefs: AppPrefs
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostUserDetailPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            val userId =
                appPrefs.postItemClickStateFlow.value?.userId ?: appAuth.authStateFlow.value?.id
            (if (userId == 0) appAuth.authStateFlow.value?.id ?: 0 else userId).let {
                if (it != null) {
                    wallControl(
                        it
                    )
                }
            }
            binding.rcView.adapter?.notifyDataSetChanged()
        }
    }


    private suspend fun wallControl(userId: Int) {
        val wallAdapter = UserWallAdapter(this)
        binding.rcView.apply {
            postVM.getWallByUserId(userId).observe(viewLifecycleOwner){posts->
                wallAdapter.submitList(posts)
            }
            adapter = wallAdapter
        }
    }

    override fun postItemClick(post: Post) {
        appPrefs.setPostClickPostId(postId = post.id)
        when (post.attachment?.type) {
            kg.zukhridin.nework.domain.enums.AttachmentType.IMAGE -> {
                try {
                    findNavController().navigate(R.id.action_userDetailFragment_to_postDetailWithImage)
                } catch (e: Exception) {
                    findNavController().navigate(R.id.action_profileFragment_to_postDetailWithImage)
                }
            }

            kg.zukhridin.nework.domain.enums.AttachmentType.VIDEO -> {
                try {
                    findNavController().apply {
                        navigate(R.id.action_profileFragment_to_postDetailFragmentWithVideo)
                    }
                } catch (e: Exception) {
                    findNavController().navigate(R.id.action_userDetailFragment_to_postDetailFragmentWithVideo)
                }
            }

            kg.zukhridin.nework.domain.enums.AttachmentType.AUDIO -> {
                try {
                    findNavController().navigate(R.id.action_profileFragment_to_postDetailFragmentWithAudio)
                } catch (e: Exception) {
                    findNavController().navigate(R.id.action_userDetailFragment_to_postDetailFragmentWithAudio)
                }
            }

            else -> {
                try {
                    findNavController().navigate(R.id.action_profileFragment_to_postDetailFragmentWithoutAttachment)
                } catch (e: Exception) {
                    findNavController().navigate(R.id.action_userDetailFragment_to_postDetailFragmentWithoutAttachment)
                }
            }
        }
    }

}
package kg.zukhridin.nework.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.adapter.UserWallAdapter
import kg.zukhridin.nework.adapter.viewholders.PostItemClickListener
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentPostPagerBinding
import kg.zukhridin.nework.dto.AttachmentType
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.utils.AppPrefs
import kg.zukhridin.nework.utils.USER
import kg.zukhridin.nework.utils.USER_ID
import kg.zukhridin.nework.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.internal.notify
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class PostPagerFragment : Fragment(), PostItemClickListener {
    private lateinit var binding: FragmentPostPagerBinding
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
        binding = FragmentPostPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            val userId = appPrefs.postItemCLickStateFlow.value?.userId ?: appAuth.authStateFlow.value?.id
            (if (userId == 0) appAuth.authStateFlow.value?.id ?: 0 else userId)?.let {
                wallControl(
                    it
                )
            }
        }
    }


    private suspend fun wallControl(userId: Int) {
        val map = postVM.getWall(userId)
        val posts = map[userId]
        val wallAdapter = UserWallAdapter(this)
        binding.rcView.apply {
            adapter = wallAdapter
            posts?.let { wallAdapter.submitList(it) }
        }
    }

    override fun postItemClick(post: Post) {
        println("post.id: ${post.id}")
        appPrefs.setPostClickPostId(postId = post.id)
        when (post.attachment?.type) {
            AttachmentType.IMAGE -> {
                try {
                    findNavController().navigate(R.id.action_userDetailFragment_to_postDetailWithImage)
                } catch (e: Exception) {
                    findNavController().navigate(R.id.action_profileFragment_to_postDetailWithImage)
                }
            }
            AttachmentType.VIDEO -> {
                try {
                    findNavController().apply {
                        navigate(R.id.action_profileFragment_to_postDetailFragmentWithVideo)
                    }
                } catch (e: Exception) {
                    findNavController().navigate(R.id.action_userDetailFragment_to_postDetailFragmentWithVideo)
                }
            }
            AttachmentType.AUDIO -> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        println("onDestroyView")
    }
}
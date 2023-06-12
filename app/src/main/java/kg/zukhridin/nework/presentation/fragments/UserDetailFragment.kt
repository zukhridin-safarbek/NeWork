package kg.zukhridin.nework.presentation.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.presentation.adapters.PagerAdapter
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentUserProfileBinding
import kg.zukhridin.nework.data.util.AppPrefs
import kg.zukhridin.nework.data.util.Constants.USER
import kg.zukhridin.nework.data.util.Constants.USER_DETAIL_FRAGMENT
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.domain.models.User
import kg.zukhridin.nework.presentation.utils.changeHeaderBackground
import kg.zukhridin.nework.presentation.utils.showShortToast
import kg.zukhridin.nework.presentation.viewmodel.PostViewModel
import kg.zukhridin.nework.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private val postVM: PostViewModel by viewModels()
    private var userId = 0
    private val userPostsWithImage = mutableListOf<Post>()
    private val userPostsWithVideo = mutableListOf<Post>()
    private val userPostsWithAudio = mutableListOf<Post>()
    private val userPostsWithWithoutAttachment = mutableListOf<Post>()
    private val userPosts = mutableListOf<Post>()
    private val userVM: UserViewModel by viewModels()
    private lateinit var ref: SharedPreferences

    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var appPrefs: AppPrefs
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        binding.titleScreen.text = getString(R.string.user_detail)
        userPostsWithImage.clear()
        userPostsWithVideo.clear()
        userPostsWithAudio.clear()
        userPostsWithWithoutAttachment.clear()
        ref = requireActivity().getSharedPreferences(USER, MODE_PRIVATE)
        appPrefs.apply {
            setFragmentName(USER_DETAIL_FRAGMENT)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = appPrefs.postItemClickStateFlow.value?.userId ?: 0
        println("user: ${appPrefs.postItemClickStateFlow.value?.userId}")
        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }
        lifecycleScope.launchWhenCreated {
            viewControl()
        }

    }

    override fun onStart() {
        super.onStart()
        tabLayout()
    }

    private fun tabLayout() = with(binding) {
        val fragments = arrayListOf<Fragment>()
        fragments.clear()
        fragments.add(PostUserDetailPagerFragment())
        fragments.add(JobUserDetailPagerFragment())
        val pagerAdapter = PagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            fragments
        )
        viewPager.adapter = pagerAdapter


        TabLayoutMediator(tabLayout, viewPager) { tab, index ->
            tab.text = when (index) {
                0 -> {
                    getString(R.string.post)
                }

                1 -> {
                    getString(R.string.job)
                }

                else -> {
                    throw Exception("Zukh error")
                }
            }
        }.attach()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showShortToast(requireContext(), tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit

        })
    }

    private fun getAvatar(url: String) {
        Glide.with(binding.userAvatar).load(url).centerCrop()
            .into(binding.userAvatar)
    }

    private suspend fun viewControl() {
        if (userId != 0) {
            val user = userVM.getUser(userId)
            wallControl(user)
            setVisibility()

        }
    }

    private suspend fun wallControl(user: User?) {
        changeHeaderBackground(
            user?.avatar
                ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg",
            binding.header
        )
        if (user != null) {
            binding.userLoginDetail.text = user.login
        }
        getAvatar(
            user?.avatar
                ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg"
        )
        postVM.getWallByUserId(userId).observe(viewLifecycleOwner) { posts ->
            println("posts: $posts")
            posts?.map { post ->
                userPosts.add(post)
                when (post.attachment?.type) {
                    kg.zukhridin.nework.domain.enums.AttachmentType.IMAGE -> {
                        userPostsWithImage.add(post)
                    }

                    kg.zukhridin.nework.domain.enums.AttachmentType.VIDEO -> userPostsWithVideo.add(
                        post
                    )

                    kg.zukhridin.nework.domain.enums.AttachmentType.AUDIO -> userPostsWithAudio.add(
                        post
                    )

                    else -> {
                        userPostsWithWithoutAttachment.add(post)
                    }
                }
            }

        }
        with(binding) {
            postVM.getWallByUserId(userId)
            username.text = user?.name
            binding.posts.text = userPosts.size.toString()
            images.text = userPostsWithImage.size.toString()
            videos.text = userPostsWithVideo.size.toString()
            audios.text = userPostsWithAudio.size.toString()
            withoutMedia.text = userPostsWithWithoutAttachment.size.toString()
        }
    }

    private fun setVisibility() = with(binding) {
        motionLayout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

}
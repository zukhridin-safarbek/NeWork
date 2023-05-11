package kg.zukhridin.nework.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.LaunchActivity
import kg.zukhridin.nework.R
import kg.zukhridin.nework.adapter.PagerAdapter
import kg.zukhridin.nework.custom.CustomToast
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentUserProfileBinding
import kg.zukhridin.nework.dto.AttachmentType
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.utils.*
import kg.zukhridin.nework.viewmodel.JobViewModel
import kg.zukhridin.nework.viewmodel.PostViewModel
import kg.zukhridin.nework.viewmodel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private val userPostsWithImage = mutableListOf<Post>()
    private val userPostsWithVideo = mutableListOf<Post>()
    private val userPostsWithAudio = mutableListOf<Post>()
    private val userPostsWithWithoutAttachment = mutableListOf<Post>()
    private lateinit var ref: SharedPreferences

    @Inject
    lateinit var appAuth: AppAuth
    @Inject
    lateinit var appPrefs: AppPrefs
    private val postVM: PostViewModel by viewModels()
    private val userVM: UserViewModel by viewModels()
    private val jobVM: JobViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        ref = requireActivity().getSharedPreferences(USER, Context.MODE_PRIVATE)
        userPostsWithImage.clear()
        userPostsWithVideo.clear()
        userPostsWithAudio.clear()
        userPostsWithWithoutAttachment.clear()
        appPrefs.apply {
            setFragmentName(PROFILE_FRAGMENT)
            appAuth.authStateFlow.value?.id?.let { setPostClickUserId(it) }
        }
        binding.titleScreen.text = getString(R.string.profile)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.userMenu.setOnClickListener {
            showDialog()
        }
        lifecycleScope.launchWhenCreated {
            viewControl()
        }
        tabLayout()
    }

    private fun tabLayout() = with(binding) {
        val fragments = arrayListOf<Fragment>()
        fragments.add(PostPagerFragment())
        fragments.add(JobPagerFragment())
        viewPager.adapter =
            PagerAdapter(requireActivity().supportFragmentManager, lifecycle, fragments)
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
                CustomToast.showShort(requireContext(), tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit

        })
    }

    override fun onStop() {
        super.onStop()
    }

    private fun getAvatar(url: String) {
        com.bumptech.glide.Glide.with(binding.userAvatar).load(url).centerCrop()
            .into(binding.userAvatar)
    }

    private suspend fun viewControl() {
        val userId = appAuth.authStateFlow.value?.id ?: 0
        val user = userVM.getUser(userId)
        wallControl(user)
        setVisibility()
    }

    private suspend fun wallControl(user: User?) {
        changeHeaderBackground(
            user?.avatar
                ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg",
            binding.header
        )
        val map = user?.id?.let { postVM.getWall(it) }
        val posts = map?.get(user.id)
        if (user != null) {
            binding.userLoginDetail.text = user.login
        }
        getAvatar(
            user?.avatar
                ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg"
        )
        posts?.map { post ->
            when (post.attachment?.type) {
                AttachmentType.IMAGE -> {
                    userPostsWithImage.add(post)
                }
                AttachmentType.VIDEO -> userPostsWithVideo.add(post)
                AttachmentType.AUDIO -> userPostsWithAudio.add(post)
                else -> {
                    userPostsWithWithoutAttachment.add(post)
                }
            }
        }
        with(binding) {
            username.text = user?.name
            binding.posts.text = posts?.size.toString()
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

    private fun showDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.bottom_sheet_account_settings)
            val logout = dialog.findViewById<MaterialButton>(R.id.logout)
            val addJob = dialog.findViewById<MaterialButton>(R.id.addJob)
            addJob?.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_addJobFragment)
                dialog.dismiss()
            }
            if (appAuth.authStateFlow.value?.id != 0 || appAuth.authStateFlow.value?.id != null) {
                logout?.visibility = View.VISIBLE
                logout?.setOnClickListener {
                    appAuth.removeAuth()
                    dismiss()
                    val intent = Intent(requireContext(), LaunchActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }
            } else {
                logout?.visibility = View.GONE
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.setGravity(Gravity.BOTTOM)
            show()
            setCanceledOnTouchOutside(true)
        }
    }
}
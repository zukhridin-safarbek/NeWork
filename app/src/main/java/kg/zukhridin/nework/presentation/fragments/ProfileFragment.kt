package kg.zukhridin.nework.presentation.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.presentation.activities.LaunchActivity
import kg.zukhridin.nework.R
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentUserProfileBinding
import kg.zukhridin.nework.data.util.AppPrefs
import kg.zukhridin.nework.data.util.Constants.PROFILE_FRAGMENT
import kg.zukhridin.nework.data.util.Constants.USER
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.domain.models.User
import kg.zukhridin.nework.presentation.utils.changeHeaderBackground
import kg.zukhridin.nework.presentation.viewmodel.JobViewModel
import kg.zukhridin.nework.presentation.viewmodel.PostViewModel
import kg.zukhridin.nework.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        fragments.add(PostUserDetailPagerFragment())
        fragments.add(JobUserDetailPagerFragment())
        viewPager.adapter =
            kg.zukhridin.nework.presentation.adapters.PagerAdapter(
                requireActivity().supportFragmentManager,
                lifecycle,
                fragments
            )
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
        if (user != null) {
            binding.userLoginDetail.text = user.login
        }
        getAvatar(
            user?.avatar
                ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg"
        )
        (user?.id ?: appAuth.authStateFlow.value?.id)?.let {
            postVM.getWallByUserId(userId = it).observe(viewLifecycleOwner) { posts ->
                posts?.map { post ->
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
        }
        with(binding) {
            username.text = user?.name
            binding.posts.text = (user?.id ?: appAuth.authStateFlow.value?.id)?.let {
                postVM.getWallByUserId(userId = it).value?.size.toString()
            }
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
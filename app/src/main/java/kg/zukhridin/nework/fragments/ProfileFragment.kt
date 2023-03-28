package kg.zukhridin.nework.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.LaunchActivity
import kg.zukhridin.nework.R
import kg.zukhridin.nework.adapter.WallWithAttachmentGalleryAdapter
import kg.zukhridin.nework.custom.Glide
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentProfileBinding
import kg.zukhridin.nework.viewmodel.PostViewModel
import kg.zukhridin.nework.viewmodel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var appAuth: AppAuth
    private val userVM: UserViewModel by viewModels()

    private val postVM: PostViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.menuBottomSheetAccountActions.setOnClickListener {
            showDialog()
        }
        Glide.start(binding.authorAvatar, appAuth.authStateFlow.value?.avatar)
        val wallAdapter = WallWithAttachmentGalleryAdapter()
        binding.rcView.adapter = wallAdapter
        postVM.posts.observe(viewLifecycleOwner) {
            binding.myPosts.text = it.size.toString()
            wallAdapter.submitList(it)
        }
        userVM.user.observe(viewLifecycleOwner) { list ->
            println("list: $list")
            list?.map {
                if (appAuth.authStateFlow.value?.id == it.id) {
                    binding.username.text = it.login
                }
            }
        }
        binding.author.text = appAuth.authStateFlow.value?.name
    }

    private fun showDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.bottom_sheet_account_settings)
            val logout = dialog.findViewById<MaterialButton>(R.id.logout)
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
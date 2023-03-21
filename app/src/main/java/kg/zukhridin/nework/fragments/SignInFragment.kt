package kg.zukhridin.nework.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.MainActivity
import kg.zukhridin.nework.R
import kg.zukhridin.nework.custom.CustomToast
import kg.zukhridin.nework.custom.OpenActivity
import kg.zukhridin.nework.databinding.FragmentSignInBinding
import kg.zukhridin.nework.viewmodel.UserViewModel

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val userVM: UserViewModel by viewModels()
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), "${it.resultCode}", Toast.LENGTH_SHORT).show()
                }
                Activity.RESULT_OK -> {
                    val uri = it.data?.data
                    userVM.savePhoto(uri, uri?.toFile())
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signIn.setOnClickListener {
            userSignIn()
        }

        binding.cameraBtn.setOnClickListener {
            takePhotoFromCamera()
        }
        setImageToPreview()
        binding.galleryBtn.setOnClickListener {
            takePhotoFromGallery()
        }

        binding.clearImg.setOnClickListener {
            savePhotoNull()
        }
        binding.askAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_authenticationFragment)
        }
    }

    private fun setImageToPreview() {
        userVM.photo.observe(viewLifecycleOwner) {
            binding.groupImg.isVisible = it?.uri != null
            binding.img.setImageURI(it?.uri)
        }
    }

    private fun takePhotoFromCamera() {
        ImagePicker.Builder(this).apply {
            cameraOnly()
            galleryMimeTypes(
                arrayOf("image/jpeg", "image/png")
            )
            crop(1f, 1f)
            maxResultSize(2048, 2048)
            createIntent(imageLauncher::launch)
        }
    }

    private fun takePhotoFromGallery() {
        ImagePicker.Builder(this).apply {
            galleryOnly()
            galleryMimeTypes(
                arrayOf("image/jpeg", "image/png")
            )
            crop(1f, 1f)
            maxResultSize(2048, 2048)
            createIntent(imageLauncher::launch)
        }
    }

    private fun userSignIn() {
        userSignInFromVM()
        userVM.userAuthResponseCode.observe(viewLifecycleOwner) {
            if (it.code.toString()[0] == '2') {
                savePhotoNull()
                OpenActivity.startFromFragment(requireActivity(), MainActivity::class.java)
            } else {
                CustomToast.showShort(
                    requireContext(),
                    "code - ${it.code}, message - ${it.message}"
                )
            }
        }
    }

    private fun userSignInFromVM() {
        userVM.userSignIn(
            binding.login.text.toString(),
            binding.password.text.toString(),
            binding.name.text.toString()
        )
    }

    private fun savePhotoNull() {
        userVM.savePhoto(null, null)
    }


}
package kg.zukhridin.nework.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentNewPostBinding
import kg.zukhridin.nework.dto.Attachment
import kg.zukhridin.nework.dto.AttachmentType
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.model.PhotoModel
import kg.zukhridin.nework.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class NewPostFragment : Fragment() {
    private lateinit var binding: FragmentNewPostBinding
    private val postVM: PostViewModel by viewModels()
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), "${it.resultCode}", Toast.LENGTH_SHORT).show()
                }
                Activity.RESULT_OK -> {
                    val uri = it.data?.data
                    postVM.savePhoto(uri, uri?.toFile())
                }
            }
        }

    @Inject
    lateinit var appAuth: AppAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postVM.photo.observe(viewLifecycleOwner) {
            insertPostToServer(
                Post(
                    0,
                    appAuth.authStateFlow.value?.id ?: 0,
                    appAuth.authStateFlow.value?.name ?: "name",
                    appAuth.authStateFlow.value?.avatar,
                    null,
                    "override fun onCreateView(\n" +
                            "        inflater: LayoutInflater,\n" +
                            "        container: ViewGroup?,\n" +
                            "        savedInstanceState: Bundle?,\n" +
                            "    ): View {\n" +
                            "        binding = FragmentNewPostBinding.inflate(inflater, container, false)\n" +
                            "        return binding.root\n" +
                            "    }",
                    "2023-03-19T19:03:647241Z",
                    null,
                    null,
                    mentionedByMe = false,
                    ownedByMe = true,
                    likedByMe = false
                ),

                it
            )
        }
        println("token: ${appAuth.authStateFlow.value?.token}")
    }

    private fun insertPostToServer(post: Post, photoModel: PhotoModel?) {
//        if (photoModel != null) {
//            postVM.insertPostToServer(
//                post.copy(
//                    attachment = Attachment(
//                        photoModel.uri.toString(),
//                        type = AttachmentType.IMAGE
//                    )
//                )
//            )
//        } else {
//            postVM.insertPostToServer(
//                post
//            )
//        }
    }
}
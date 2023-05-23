package kg.zukhridin.nework.presentation.fragments

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentNewPostBinding
import kg.zukhridin.nework.domain.enums.AttachmentType
import kg.zukhridin.nework.domain.enums.StatusType
import kg.zukhridin.nework.domain.models.*
import kg.zukhridin.nework.presentation.adapter.MentionPeopleAdapter
import kg.zukhridin.nework.presentation.adapter.MentionPeopleItemListener
import kg.zukhridin.nework.presentation.fragments.NewPostGalleryFragment.Companion.file
import kg.zukhridin.nework.presentation.utils.hideKeyboard
import kg.zukhridin.nework.presentation.viewmodel.EventViewModel
import kg.zukhridin.nework.presentation.viewmodel.PostViewModel
import kg.zukhridin.nework.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class NewPostFragment : Fragment(), NewPostFragmentDialogItemClickListener,
    MentionPeopleItemListener {
    private lateinit var binding: FragmentNewPostBinding
    private val postVM: PostViewModel by viewModels()
    private val userVM: UserViewModel by viewModels()
    private val eventVM: EventViewModel by viewModels()
    private val mentionedIds = mutableListOf<Int>()

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
        lifecycleScope.launchWhenCreated {
            val user = appAuth.authStateFlow.value?.id?.let { userVM.getUser(it) }
            getImages(user?.avatar)
            addPost(user)
            closeBtn()
            tagPeople()
            checkPostOrEvent()
        }
        binding.checkPostOrEvent.setOnClickListener {
            checkPostOrEvent()
        }

    }

    private fun checkPostOrEvent() {
        if (binding.checkPostOrEvent.isChecked) {
            binding.fragmentName.text = "${getString(R.string.str_new)}${getString(R.string.event)}"
            binding.checkPostOrEvent.text = getString(R.string.event)
            binding.checkPostOrEvent.setBackgroundColor(Color.TRANSPARENT)
        } else {
            binding.fragmentName.text = "${getString(R.string.str_new)}${getString(R.string.post)}"
            binding.checkPostOrEvent.text = getString(R.string.post)
        }
    }

    private fun tagPeople() = with(binding) {
        binding.tagPeople.setOnClickListener {
            mentionContainer.visibility = View.VISIBLE
            val mentioned = mutableListOf<String>()
            val array = mutableListOf<String>()
            userVM.users.observe(viewLifecycleOwner) { list ->
                list?.map { user ->
                    array.add(user.login)
                }
            }
            ArrayAdapter(
                requireContext(),
                R.layout.media_type_drop_down_item,
                array
            ).also { adapter ->
                mentionField.setAdapter(adapter)
            }
            mentionField.setOnItemClickListener { _, _, _, _ ->
                if (mentionField.text.isNotBlank()) {
                    userVM.users.observe(viewLifecycleOwner) { list ->
                        list?.map { user ->
                            if (user.login == mentionField.text.toString()) {
                                mentioned.add(user.login)
                                mentionedIds.add(user.id)
                            }

                        }
                    }
                }
                mentionField.setText("")
                mentionField.clearFocus()
                hideKeyboard()
            }
            val mentionAdapter = MentionPeopleAdapter(
                mentioned,
                this@NewPostFragment
            )
            mentionRcView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            mentionRcView.apply {
                adapter = mentionAdapter
            }
            mentionCloseBtn.setOnClickListener {
                mentionContainer.visibility = View.GONE
            }
        }
    }

    private suspend fun addPost(user: User?) = with(binding) {
        println(appAuth.authStateFlow.value?.token)
        val file = arguments?.file
        var type: String? = null
        val extension: String? =
            if (file == null) null else MimeTypeMap.getFileExtensionFromUrl(file)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        val mimeType: List<String>? = type?.split(Regex("/"))
        add.setOnClickListener {
            val post = Post(
                0,
                appAuth.authStateFlow.value?.id ?: 0,
                user?.name ?: "name",
                user?.avatar,
                null,
                binding.content.text.toString(),
                "2023-03-19T19:03:647241Z",
                null,
                null,
                attachment = if (file == null) null else Attachment(
                    file,
                    if ((mimeType?.get(0)
                            ?: 0) == "image"
                    ) kg.zukhridin.nework.domain.enums.AttachmentType.IMAGE else if ((mimeType?.get(0)
                            ?: 0) == "video"
                    ) kg.zukhridin.nework.domain.enums.AttachmentType.VIDEO else kg.zukhridin.nework.domain.enums.AttachmentType.AUDIO
                ),
                mentionedMe = appAuth.authStateFlow.value?.id in mentionedIds,
                mentionIds = mentionedIds,
                ownedByMe = true,
                likedByMe = false
            )
            val event = Event(
                id = 0,
                content = binding.content.text.toString(),
                datetime = "2023-05-19T17:55:16.816Z",
                attachment = if (file == null) null else Attachment(
                    file,
                    if ((mimeType?.get(0)
                            ?: 0) == "image"
                    ) kg.zukhridin.nework.domain.enums.AttachmentType.IMAGE else if ((mimeType?.get(0)
                            ?: 0) == "video"
                    ) kg.zukhridin.nework.domain.enums.AttachmentType.VIDEO else kg.zukhridin.nework.domain.enums.AttachmentType.AUDIO
                ),
               type = StatusType.ONLINE, participatedByMe = false
            )
            lifecycleScope.launchWhenCreated {
                val res =
                    if (!binding.checkPostOrEvent.isChecked) insertPostToServer(post) else insertEventToServer(
                        event
                    )
                if (res) {
                    findNavController().navigate(R.id.action_newPostWithMediaFragment_to_homeFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }

    private suspend fun insertEventToServer(event: Event): Boolean {
        return eventVM.insertEvent(event).first
    }

    private fun closeBtn() {
        binding.close.setOnClickListener {
            findNavController().navigate(R.id.action_newPostWithMediaFragment_to_newPostGalleryFragment)
        }
    }

    private fun getImages(url: String?) = with(binding) {
        Glide.with(authorAvatar).load(url).into(authorAvatar)
        val arg = arguments?.file
        if (arg != null) {
            contentImage.visibility = View.VISIBLE
            if ("file" !in arg.toString()) {
                val file = File(arg)
                val uri = Uri.fromFile(file)
                Glide.with(contentImage).load(uri).into(contentImage)
            } else {
                val uri = Uri.parse(arg)
                Glide.with(contentImage).load(uri).into(contentImage)
            }
        } else {
            contentImage.visibility = View.GONE
        }
    }

    private suspend fun insertPostToServer(post: Post): Boolean {
        return postVM.insertPost(
            post
        )
    }


    override fun onItemClick(item: String?) {
        Toast.makeText(requireContext(), item, Toast.LENGTH_SHORT).show()
    }

    override fun itemClick(mentioned: String) {
        println("men: $mentioned")
    }
}
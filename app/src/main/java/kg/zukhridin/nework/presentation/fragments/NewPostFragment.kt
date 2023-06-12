package kg.zukhridin.nework.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.data.util.Constants.MY_LOG
import kg.zukhridin.nework.databinding.FragmentNewPostBinding
import kg.zukhridin.nework.domain.enums.StatusType
import kg.zukhridin.nework.domain.models.*
import kg.zukhridin.nework.presentation.adapters.MentionPeopleAdapter
import kg.zukhridin.nework.presentation.adapters.MentionPeopleItemListener
import kg.zukhridin.nework.presentation.fragments.NewPostGalleryFragment.Companion.file
import kg.zukhridin.nework.presentation.utils.CustomOffsetDateTime
import kg.zukhridin.nework.presentation.utils.Permissions
import kg.zukhridin.nework.presentation.utils.hideKeyboard
import kg.zukhridin.nework.presentation.viewmodel.EventViewModel
import kg.zukhridin.nework.presentation.viewmodel.PostViewModel
import kg.zukhridin.nework.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
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
    private var currentLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var permissions: Permissions

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (isLocationPermissionGranted()) {
            getCurrentLocationUser()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            val user = appAuth.authStateFlow.value?.id?.let { userVM.getUser(it) }
            getImages(user?.avatar)
            addPost(user)
            closeBtn()
            addLocationBtn()
            tagPeople()
            checkPostOrEvent()
        }
        binding.checkPostOrEvent.setOnClickListener {
            checkPostOrEvent()
        }

    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (!permissions.isLocationPermissionGranted(requireContext())) {
            Log.d(MY_LOG, "if")
            permissions.setLocationPermission(this)
        } else {
            Log.d(MY_LOG, "else")
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationUser() {
        try {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                }
            }
        } catch (e: Exception) {
            currentLocation = null
        }
    }

    private fun addLocationBtn() {
        binding.addLocation.setOnClickListener {
            binding.locationCheckBox.isChecked = !binding.locationCheckBox.isChecked
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
            binding.add.visibility = View.GONE
            binding.addProgress.visibility = View.VISIBLE
            val post = Post(
                0,
                appAuth.authStateFlow.value?.id ?: 0,
                user?.name ?: "name",
                user?.avatar,
                null,
                binding.content.text.toString(),
                published = CustomOffsetDateTime.timeCode(),
                coords = if (currentLocation != null) if (binding.locationCheckBox.isChecked) Coordinates(
                    "${(currentLocation!!.latitude).toFloat()}",
                    "${(currentLocation!!.longitude).toFloat()}"
                ) else null
                else null,
                null,
                attachment = if (file == null) null else Attachment(
                    file,
                    if ((mimeType?.get(0)
                            ?: 0) == "image"
                    ) kg.zukhridin.nework.domain.enums.AttachmentType.IMAGE else if ((mimeType?.get(
                            0
                        )
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
                authorId = appAuth.authStateFlow.value?.id ?: 0,
                author = user?.name ?: "name",
                authorAvatar = user?.avatar,
                content = binding.content.text.toString(),
                coords = if (currentLocation != null) if (binding.locationCheckBox.isChecked) Coordinates(
                    "${(currentLocation!!.latitude).toFloat()}",
                    "${(currentLocation!!.longitude).toFloat()}"
                ) else null
                else null,
                datetime = CustomOffsetDateTime.timeCode(),
                attachment = if (file == null) null else Attachment(
                    file,
                    if ((mimeType?.get(0)
                            ?: 0) == "image"
                    ) kg.zukhridin.nework.domain.enums.AttachmentType.IMAGE else if ((mimeType?.get(
                            0
                        )
                            ?: 0) == "video"
                    ) kg.zukhridin.nework.domain.enums.AttachmentType.VIDEO else kg.zukhridin.nework.domain.enums.AttachmentType.AUDIO
                ),
                type = StatusType.ONLINE, participatedByMe = false
            )
            lifecycleScope.launchWhenCreated {
                val res =
                    if (!binding.checkPostOrEvent.isChecked) insertPost(post) else insertEvent(
                        event
                    )
                if (res.first) {
                    findNavController().navigate(R.id.action_newPostWithMediaFragment_to_homeFragment)
                } else {
                    binding.add.visibility = View.VISIBLE
                    binding.addProgress.visibility = View.GONE
                    res.second?.content?.map {
                        binding.content.error = it
                    }
                }
            }

        }

    }

    private suspend fun insertEvent(event: Event): Pair<Boolean, ErrorResponseModel?> {
        return eventVM.insertEvent(event)
    }

    private fun closeBtn() {
        binding.close.setOnClickListener {
            findNavController().popBackStack()
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

    private suspend fun insertPost(post: Post): Pair<Boolean, ErrorResponseModel> {
        val response = postVM.insertPost(
            post
        )
        Log.d(MY_LOG, "${response.second}")
        return response
    }


    override fun onItemClick(item: String?) {
        Toast.makeText(requireContext(), item, Toast.LENGTH_SHORT).show()
    }

    override fun itemClick(mentioned: String) {
        println("men: $mentioned")
    }
}
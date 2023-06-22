package kg.zukhridin.nework.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.databinding.FragmentNewPostBinding
import kg.zukhridin.nework.domain.enums.AttachmentType.*
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
import kotlinx.coroutines.launch
import java.io.File
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class NewPostFragment : Fragment(), MentionPeopleItemListener {
    private lateinit var binding: FragmentNewPostBinding
    private val postVM: PostViewModel by viewModels()
    private val userVM: UserViewModel by viewModels()
    private val eventVM: EventViewModel by viewModels()
    private lateinit var timer: Timer
    private val mentionedIds = mutableListOf<Int>()
    private var currentLocationYan: Point? = null
    private var locationManager: LocationManager? = null
    private var myLocationListener: LocationListener? = null
    private val DESIRED_ACCURACY = 0.0
    private val MINIMAL_TIME: Long = 1000
    private val MINIMAL_DISTANCE = 1.0
    private val USE_IN_BACKGROUND = false

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
        checkLocationPermission()
        return binding.root
    }

    private fun checkLocationPermission() {
        if (!permissions.isLocationPermissionGranted(requireContext())) {
            binding.addLocation.visibility = View.GONE
            binding.addLocationPermission.visibility = View.VISIBLE
        } else {
            binding.addLocation.visibility = View.VISIBLE
            binding.addLocationPermission.visibility = View.GONE
        }
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
        binding.addLocationPermission.setOnClickListener {
            if (!permissions.isLocationPermissionGranted(requireContext())) {
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            } else {
                checkLocationPermission()
            }

        }
    }

    private fun checkPermissionOnAttach(): Boolean {
        return if (!permissions.isLocationPermissionGranted(requireContext())) {
            permissions.setLocationPermission(this)
        } else {
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationUser() {
        currentLocationYan = null
        try {
            val mapkit = MapKitFactory.getInstance()
            locationManager = mapkit.createLocationManager()
            myLocationListener = object : LocationListener {
                override fun onLocationUpdated(location: Location) {
                    currentLocationYan = location.position
                }

                override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                    Unit
                }
            }
            locationSubscribeUpdate()
        } catch (e: Exception) {
            currentLocationYan = null
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        checkPermissionOnAttach()
    }

    private fun everySecond() {
        val task = object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    binding.locationProgressbar.isVisible = currentLocationYan == null
                    binding.add.isClickable = !binding.locationProgressbar.isVisible
                    binding.locationCheckBox.isVisible = !binding.locationProgressbar.isVisible
                }
            }
        }
        if (currentLocationYan == null) {
            timer.schedule(task, 0, 1000)
        } else {
            timer.cancel()
            timer.purge()
        }
    }

    override fun onStart() {
        super.onStart()
        timer = Timer()
        MapKitFactory.getInstance().onStart()
    }

    private fun locationSubscribeUpdate() {
        if (locationManager != null && myLocationListener != null) {
            locationManager!!.subscribeForLocationUpdates(
                DESIRED_ACCURACY,
                MINIMAL_TIME,
                MINIMAL_DISTANCE,
                USE_IN_BACKGROUND,
                FilteringMode.OFF,
                myLocationListener!!
            )
        }
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        if (locationManager != null && myLocationListener != null) {
            locationManager!!.unsubscribe(myLocationListener!!)
        }
        timer.cancel()
        timer.purge()
    }

    private fun addLocationBtn() {
        binding.addLocation.setOnClickListener {
            if (!binding.locationCheckBox.isChecked) {
                getCurrentLocationUser()
                binding.locationProgressbar.visibility = View.VISIBLE
                binding.locationCheckBox.visibility = View.GONE
            } else {
                binding.locationProgressbar.visibility = View.GONE
                binding.locationCheckBox.visibility = View.VISIBLE
            }
            binding.locationCheckBox.isChecked = !binding.locationCheckBox.isChecked
            everySecond()
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
                mentioned, this@NewPostFragment
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

    private fun addPost(user: User?) = with(binding) {
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
                coords = currentLocationYan?.let { currentLocationYan ->
                    if (binding.locationCheckBox.isChecked) {
                        Coordinates(
                            "${(currentLocationYan.latitude).toFloat()}",
                            "${(currentLocationYan.longitude).toFloat()}",
                        )
                    } else {
                        null
                    }
                },
                null,
                attachment = if (file == null) null else Attachment(
                    file,
                    if ((mimeType?.get(0)
                            ?: 0) == "image"
                    ) IMAGE else if ((mimeType?.get(
                            0
                        )
                            ?: 0) == "video"
                    ) VIDEO else AUDIO
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
                coords = if (currentLocationYan != null) if (binding.locationCheckBox.isChecked) Coordinates(
                    "${(currentLocationYan!!.latitude).toFloat()}",
                    "${(currentLocationYan!!.longitude).toFloat()}"
                ) else null
                else null,
                datetime = CustomOffsetDateTime.timeCode(),
                attachment = if (file == null) null else Attachment(
                    file,
                    if ((mimeType?.get(0)
                            ?: 0) == "image"
                    ) IMAGE else if ((mimeType?.get(
                            0
                        )
                            ?: 0) == "video"
                    ) VIDEO else AUDIO
                ),
                type = StatusType.ONLINE, participatedByMe = false
            )
            val res =
                if (!binding.checkPostOrEvent.isChecked) insertPost(post) else insertEvent(
                    event
                )
            res.let { result ->
                if (result.first == true) {
                    lifecycleScope.launch {
                        postVM.insertPostToStorage(post)
                    }
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

    private fun insertEvent(event: Event): Pair<Boolean?, ErrorResponseModel?> {
        eventVM.insertEvent(event)
        var first: Boolean? = null
        var second: ErrorResponseModel? = null
        eventVM.responseIsSuccessFull.observe(viewLifecycleOwner) { bool->
            bool?.let {
                first = it
            }
        }
        eventVM.responseReason.observe(viewLifecycleOwner) { reason ->
            reason?.let {
                second = it
            }
        }
        return Pair(first, second)
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

    private fun insertPost(post: Post): Pair<Boolean?, ErrorResponseModel?> {
        postVM.insertPost(post)
        val first = postVM.responseIsSuccessFull.value
        val second = postVM.responseReason.value
        return Pair(first, second)
    }

    override fun removeItem(item: String) {
        TODO("Not yet implemented")
    }
}
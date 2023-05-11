package kg.zukhridin.nework.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.adapter.GalleryAdapter
import kg.zukhridin.nework.adapter.OnImageItemClickListener
import kg.zukhridin.nework.databinding.FragmentNewPostGalleryBinding
import kg.zukhridin.nework.dto.CustomMedia
import kg.zukhridin.nework.dto.CustomMediaType
import kg.zukhridin.nework.utils.DataTransferArg
import kg.zukhridin.nework.utils.Permissions
import kg.zukhridin.nework.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class NewPostGalleryFragment : Fragment(), OnImageItemClickListener, Player.Listener {
    private lateinit var binding: FragmentNewPostGalleryBinding
    private val postVM: PostViewModel by viewModels()
    private lateinit var mediaAdapter: GalleryAdapter
    private lateinit var medias: ArrayList<CustomMedia>
    private lateinit var file: File
    private lateinit var fileToNext: File
    private lateinit var uriToNext: Uri
    private lateinit var timer: Timer

    @Inject
    lateinit var permissions: Permissions

    @Inject
    lateinit var player: ExoPlayer
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                ImagePicker.RESULT_ERROR -> {
                    error("ImagePicker error")
                }
                Activity.RESULT_OK -> {
                    findNavController().navigate(
                        R.id.action_newPostGalleryFragment_to_newPostFragment,
                        Bundle().apply {
                            file = result.data?.data.toString()
                        })
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPostGalleryBinding.inflate(inflater, container, false)
        timer = Timer()
        checkFolderPermission()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!permissions.isFolderPermissionGranted(requireContext())) {
            val timerTask = object : TimerTask() {
                override fun run() {
                    requireActivity().runOnUiThread {
                        checkFolderPermission()
                    }
                }
            }
            timer.schedule(timerTask, 0, 1000)
        }
        setFolderPermissionButton()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
        timer.purge()
    }

    private fun setFolderPermissionButton() {
        binding.setFolderPermission.setOnClickListener {
            permissions.folderPermission(requireActivity())
        }
    }

    private fun checkFolderPermission() {
        if (permissions.isFolderPermissionGranted(requireContext())) {
            binding.body.visibility = View.VISIBLE
            binding.topBar.visibility = View.VISIBLE
            binding.permissionDenied.visibility = View.GONE
            binding.createPostWithoutMediaWhenPermissionDenied.visibility = View.GONE
            binding.setFolderPermission.visibility = View.GONE
            controlGallery()
            rcViewImageControl()
            nextBtn()
            closeBtn()
            cameraBtn()
            createNewPostWithoutMediaButtons()
            timer.cancel()
            timer.purge()
        } else {
            binding.setFolderPermission.visibility = View.VISIBLE
            binding.body.visibility = View.GONE
            binding.topBar.visibility = View.GONE
            binding.permissionDenied.visibility = View.VISIBLE
            binding.createPostWithoutMediaWhenPermissionDenied.visibility = View.VISIBLE
            createNewPostWithoutMediaButtons()
        }
    }

    private fun createNewPostWithoutMediaButtons() {
        binding.createPostWithoutMediaWhenPermissionDenied.setOnClickListener {
            createNewPostWithoutMedia()
        }
        binding.createPostWithoutMedia.setOnClickListener {
            createNewPostWithoutMedia()
        }
    }

    private fun createNewPostWithoutMedia() {
        findNavController().navigate(R.id.action_newPostGalleryFragment_to_newPostFragment)
    }

    private fun cameraBtn() {
        binding.camera.setOnClickListener {
            camera()
        }
    }

    private fun camera() {
        ImagePicker.Builder(this).apply {
            cameraOnly()
            galleryMimeTypes(arrayOf("image/jpeg", "image/png"))
            maxResultSize(2048, 2048)
            crop(1f, 1f)
            saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM)!!)
            createIntent(launcher::launch)
        }
    }

    private fun nextBtn() = with(binding) {
        next.setOnClickListener {
            findNavController().navigate(
                R.id.action_newPostGalleryFragment_to_newPostFragment,
                Bundle().apply {
                    file = fileToNext.toString()
                })
        }
    }

    private fun closeBtn() {
        binding.close.setOnClickListener {
            findNavController().navigate(R.id.action_newPostGalleryFragment_to_homeFragment)
        }
    }

    private fun controlGallery() {
        rcViewImageControl()
        val items = listOf("Image", "Video", "Audio")
        val adapter = ArrayAdapter(requireContext(), R.layout.media_type_drop_down_item, items)
        binding.autoComplete.setAdapter(adapter)
        binding.autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                when (parent.getItemAtPosition(position)) {
                    items[0] -> {
                        rcViewImageControl()
                    }
                    items[1] -> {
                        rcViewVideoControl()
                    }
                    items[2] -> {
                        rcViewAudioControl()
                    }
                }
            }

    }

    private fun rcViewVideoControl() {
        binding.rcViewVideo.visibility = View.VISIBLE
        binding.rcViewImage.visibility = View.GONE
        postVM.mediasFromGallery(requireContext(), CustomMediaType.VIDEO)
        medias = postVM.mediasFromExternalStorage.value ?: ArrayList()
        if (medias.isNotEmpty()) {
            binding.rcViewVideo.visibility = View.VISIBLE
            binding.rcViewImage.visibility = View.GONE
            binding.galleryEmptyText.visibility = View.GONE
            file = File(medias.last().url)
            val uri = Uri.fromFile(file)
            fileToNext = file
            uriToNext = uri
            if (medias.last().type == CustomMediaType.VIDEO) {
                binding.galleryVideoView.visibility = View.VISIBLE
                binding.galleryImageView.visibility = View.GONE
                binding.galleryVideoView.player = player
                val mediaItem = MediaItem.fromUri(medias.last().url)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = true
                binding.galleryVideoView.useController = true
            }
            mediaAdapter = medias.let {
                it.reverse()
                GalleryAdapter(it, this)
            }
            binding.rcViewVideo.apply {
                setHasFixedSize(true)
                if (medias.size > 8) {
                    addItemDecoration(
                        DividerItemDecoration(
                            this.context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    addItemDecoration(
                        DividerItemDecoration(
                            this.context,
                            DividerItemDecoration.HORIZONTAL
                        )
                    )
                }
                adapter = mediaAdapter
            }
        } else {
            binding.galleryEmptyText.visibility = View.VISIBLE
            binding.rcViewVideo.visibility = View.GONE
            binding.rcViewImage.visibility = View.GONE
        }
    }

    private fun rcViewImageControl() {
        postVM.mediasFromGallery(requireContext(), CustomMediaType.IMAGE)
        medias = postVM.mediasFromExternalStorage.value ?: ArrayList()
        if (medias.isNotEmpty()) {
            binding.galleryEmptyText.visibility = View.GONE
            binding.rcViewVideo.visibility = View.GONE
            binding.rcViewImage.visibility = View.VISIBLE
            file = File(medias.last().url)
            val uri = Uri.fromFile(file)
            fileToNext = file
            uriToNext = uri
            if (medias.last().type == CustomMediaType.IMAGE) {
                binding.galleryVideoView.visibility = View.GONE
                binding.galleryImageView.visibility = View.VISIBLE
                Glide.with(requireActivity()).load(uri)
                    .into(binding.galleryImageView)
            }
            mediaAdapter = medias.let {
                it.reverse()
                GalleryAdapter(it, this)
            }
            binding.rcViewImage.apply {
                setHasFixedSize(true)
                if (medias.size > 8) {
                    addItemDecoration(
                        DividerItemDecoration(
                            this.context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    addItemDecoration(
                        DividerItemDecoration(
                            this.context,
                            DividerItemDecoration.HORIZONTAL
                        )
                    )
                }
                adapter = mediaAdapter
            }
        } else {
            binding.galleryEmptyText.visibility = View.VISIBLE
            binding.rcViewVideo.visibility = View.GONE
            binding.rcViewImage.visibility = View.GONE
        }
    }

    private fun rcViewAudioControl() {
        postVM.mediasFromGallery(requireContext(), CustomMediaType.AUDIO)
        medias = postVM.mediasFromExternalStorage.value ?: ArrayList()
        if (medias.isNotEmpty()) {
            binding.galleryEmptyText.visibility = View.GONE
            binding.rcViewVideo.visibility = View.GONE
            binding.rcViewImage.visibility = View.VISIBLE
            file = File(medias.last().url)
            val uri = Uri.fromFile(file)
            fileToNext = file
            uriToNext = uri
            if (medias.last().type == CustomMediaType.AUDIO) {
                binding.galleryVideoView.visibility = View.VISIBLE
                binding.galleryImageView.visibility = View.GONE
                binding.galleryVideoView.player = player
                val mediaItem = MediaItem.fromUri(medias.last().url)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = true
                binding.galleryVideoView.useController = true
            }
            mediaAdapter = medias.let {
                it.reverse()
                GalleryAdapter(it, this)
            }
            binding.rcViewImage.apply {
                setHasFixedSize(true)
                if (medias.size > 8) {
                    addItemDecoration(
                        DividerItemDecoration(
                            this.context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    addItemDecoration(
                        DividerItemDecoration(
                            this.context,
                            DividerItemDecoration.HORIZONTAL
                        )
                    )
                }
                adapter = mediaAdapter
            }
        } else {
            binding.galleryEmptyText.visibility = View.VISIBLE
            binding.rcViewVideo.visibility = View.GONE
            binding.rcViewImage.visibility = View.GONE
        }
    }

    override fun itemClick(customMedia: CustomMedia) {
        val file = File(customMedia.url)
        val uri = Uri.fromFile(file)
        fileToNext = file
        uriToNext = uri
        if (customMedia.type == CustomMediaType.IMAGE) {
            binding.galleryVideoView.visibility = View.GONE
            binding.galleryImageView.visibility = View.VISIBLE
            Glide.with(requireActivity()).load(uri).into(binding.galleryImageView)
        } else {
            binding.galleryVideoView.visibility = View.VISIBLE
            binding.galleryImageView.visibility = View.GONE
            binding.galleryVideoView.player = player
            val mediaItem = MediaItem.fromUri(customMedia.url)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            binding.galleryVideoView.useController = true
        }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
        player.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    override fun onDetach() {
        super.onDetach()
        player.stop()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    companion object {
        var Bundle.file: String? by DataTransferArg
    }
}
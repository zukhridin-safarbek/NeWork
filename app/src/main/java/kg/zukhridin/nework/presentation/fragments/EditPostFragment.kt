package kg.zukhridin.nework.presentation.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kg.zukhridin.nework.data.util.AppPrefs
import kg.zukhridin.nework.databinding.FragmentEditPostBinding
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.presentation.adapters.MentionPeopleAdapter
import kg.zukhridin.nework.presentation.adapters.MentionPeopleItemListener
import kg.zukhridin.nework.presentation.utils.hideKeyboard
import kg.zukhridin.nework.presentation.viewmodel.PostViewModel
import kg.zukhridin.nework.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class EditPostFragment : Fragment(),
    MentionPeopleItemListener {
    private lateinit var binding: FragmentEditPostBinding
    private var postId = 0
    private val postVM: PostViewModel by viewModels()
    private val userVM: UserViewModel by viewModels()
    private val mentionedLogins = mutableListOf<String>()
    private val mentionedIds = mutableListOf<Int>()
    private lateinit var mentionAdapter: MentionPeopleAdapter
    private lateinit var updatedPost: Post

    @Inject
    lateinit var appPrefs: AppPrefs
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterControl()
        tagPeople()
        postControl()
        addPost()
        closeFragment()

    }

    private fun adapterControl() {
        mentionAdapter = MentionPeopleAdapter(
            mentionedLogins,
            this@EditPostFragment
        )
        binding.mentionRcView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mentionAdapter
        }
    }

    private fun closeFragment() {
        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun addPost() {
        binding.add.setOnClickListener {
            postVM.updateById(
                post = updatedPost.copy(
                    content = binding.content.text.toString(),
                    mentionIds = mentionedIds
                )
            )
            findNavController().navigateUp()
        }
    }

    private fun postControl() = lifecycleScope.launchWhenCreated {
        postId = appPrefs.postItemClickStateFlow.value?.postId ?: 0
        if (postId != 0) {
            val post = postVM.getPostById(postId)
            if (post.id == postId) {
                with(binding) {
                    contentImage.visibility = View.VISIBLE
                    author.text = post.author
                    if (post.coords == null) {
                        coordination.text = getString(R.string.add_location)
                        coordination.setTextColor(Color.rgb(49, 140, 231))
                    } else {
                        coordination.text = post.coords!!.lat
                    }
                    post.mentionIds.map {
                        mentionedIds.add(it)
                    }
                    Glide.with(authorAvatar).load(
                        post.authorAvatar ?: "https://lookw.ru/10/1018/1566950217-3.jpg"
                    ).fitCenter().circleCrop().into(authorAvatar)
                    if (post.attachment != null) {
                        Glide.with(contentImage).load(post.attachment!!.url)
                            .into(contentImage)
                    } else {
                        contentImage.visibility = View.GONE
                    }
                    content.setText(post.content)
                    content.setSelection(post.content.length)
                    content.requestFocus()
                    updatedPost = post
                }
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun tagPeople() = with(binding) {
        userVM.users.observe(viewLifecycleOwner) { users ->
            users?.map { user ->
                mentionedIds.map {
                    if (user.id == it) {
                        mentionedLogins.add(user.login)
                    }
                }
            }
        }
        binding.tagPeople.setOnClickListener {
            mentionContainer.visibility = View.VISIBLE
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
                                if (user.login !in mentionedLogins && user.id !in mentionedIds) {
                                    mentionedIds.add(user.id)
                                    mentionedLogins.add(user.login)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.you_have_already_mentioned_this_person),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        }
                    }
                }
                mentionField.setText("")
                mentionField.clearFocus()
                hideKeyboard()
                adapterControl()
            }
            mentionCloseBtn.setOnClickListener {
                mentionContainer.visibility = View.GONE
            }
        }
    }

    override fun removeItem(item: String) {
        userVM.users.observe(viewLifecycleOwner) { users ->
            users?.map { user ->
                if (user.login == item) {
                    mentionedIds.remove(user.id)
                    mentionedLogins.remove(item)
                }
            }
        }
        adapterControl()
    }
}
package kg.zukhridin.nework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
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
        var count = 0
        println(postVM.posts.value)
        postVM.posts.observe(viewLifecycleOwner) { list ->
            for (i in list) {
                if (i.ownedByMe) {
                    count++
                }
            }
            binding.myPosts.text = count.toString()
        }
            binding.author.text = appAuth.authStateFlow.value?.name
    }
}
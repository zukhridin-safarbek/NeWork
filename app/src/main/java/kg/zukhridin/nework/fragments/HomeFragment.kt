package kg.zukhridin.nework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.adapter.PostAdapter
import kg.zukhridin.nework.adapter.PostOnItemEventsListener
import kg.zukhridin.nework.adapter.PostsLoaderStateAdapter
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.databinding.FragmentHomeBinding
import kg.zukhridin.nework.decorations.PostDividerItemDecoration
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HomeFragment : Fragment(), PostOnItemEventsListener {
    private lateinit var binding: FragmentHomeBinding
    private val postVM: PostViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var postDao: PostDao
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postAdapter = PostAdapter(this, requireActivity())
        rcViewControl()
    }

    private fun rcViewControl() = with(binding) {
        swipeRefresh.setOnRefreshListener {
            postAdapter.refresh()
        }
        rcView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                PostDividerItemDecoration(
                    binding.rcView.context,
                    PostDividerItemDecoration.VERTICAL,
                    24
                )
            )
            adapter = postAdapter.withLoadStateHeaderAndFooter(
                PostsLoaderStateAdapter(requireContext()),
                PostsLoaderStateAdapter(requireContext())
            )
        }
        lifecycleScope.launchWhenCreated {
            postVM.listData.collectLatest { pagingData ->
                postAdapter.submitData(pagingData)
            }
        }
        lifecycleScope.launchWhenCreated {
            postAdapter.loadStateFlow.collectLatest {
                swipeRefresh.isRefreshing = it.refresh is LoadState.Loading
            }
        }
    }

    override fun onLike(post: Post) {
        postVM.likeById(post)
    }
}
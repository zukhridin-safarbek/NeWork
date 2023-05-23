package kg.zukhridin.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.presentation.adapter.PagerAdapter
import kg.zukhridin.nework.databinding.FragmentHomeBinding
import kg.zukhridin.nework.presentation.utils.CheckNetwork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var timer: Timer

    @Inject
    lateinit var checkNetwork: CheckNetwork
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        timer = Timer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkNetwork.networkAvailable()) {
            binding.internetWarning.visibility = View.GONE
            tabLayout()
        } else {
            binding.internetWarning.visibility = View.VISIBLE
            checkNetworkEveryThreeSecond()
        }
    }

    private fun tabLayout() = with(binding) {
        val fragments = arrayListOf<Fragment>()
        fragments.add(PostPagerFragment())
        fragments.add(EventPagerFragment())
        viewPager.adapter =
            PagerAdapter(
                requireActivity().supportFragmentManager,
                lifecycle,
                fragments
            )
        TabLayoutMediator(tabLayout, viewPager) { tab, index ->
            tab.text = when (index) {
                0 -> {
                    getString(R.string.posts)
                }
                1 -> {
                    getString(R.string.events)
                }
                else -> {
                    throw Exception("Pager error")
                }
            }
        }.attach()
    }

    private fun checkNetworkEveryThreeSecond() {
        val task = object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    if (checkNetwork.networkAvailable()) {
                        binding.internetWarning.visibility = View.GONE
                        tabLayout()
                    } else {
                        binding.internetWarning.visibility = View.VISIBLE
                    }
                }
            }
        }
        if (!checkNetwork.networkAvailable()) {
            timer.schedule(task, 0, 1000)
        } else {
            timer.cancel()
            timer.purge()
        }
    }

}
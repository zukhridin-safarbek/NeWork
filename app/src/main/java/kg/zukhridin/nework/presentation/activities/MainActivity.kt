package kg.zukhridin.nework.presentation.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.databinding.ActivityMainBinding
import kg.zukhridin.nework.presentation.utils.CheckNetwork
import kg.zukhridin.nework.presentation.utils.Permissions
import kg.zukhridin.nework.presentation.utils.startWithBitmapCircleCrop
import kg.zukhridin.nework.presentation.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val userVM: UserViewModel by viewModels()

    @Inject
    lateinit var checkNetwork: CheckNetwork

    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var permissions: Permissions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launchWhenCreated {
            if (checkNetwork.networkAvailable()) {
                binding.bnv.visibility = View.VISIBLE
                bnvControl()
                permissions.folderPermission(this@MainActivity)
                setVisibility()
            } else {
                binding.bnv.visibility = View.GONE
            }
        }

    }

    private fun setVisibility() {
        binding.bodyContainer.visibility = View.VISIBLE
        binding.progress.visibility = View.GONE
    }

    private suspend fun bnvControl() {
        binding.bnv.itemIconTintList = null
        val navHostFragment =
            binding.navHostFragmentContainer.getFragment<Fragment>() as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(binding.bnv, navController)
        val user = userVM.getUser(appAuth.authStateFlow.value?.id.toString().toInt())
        if (user != null){
            startWithBitmapCircleCrop(
                this,
                user.avatar ?: "https://zolya.ru/wp-content/uploads/9/8/7/9877e898924c3914b792bfbd83eaa65c.jpeg",
                binding.bnv.menu.findItem(R.id.profileFragment),
                resources
            )
        }

    }
}
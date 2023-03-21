package kg.zukhridin.nework

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.custom.Glide
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.databinding.ActivityMainBinding
import kg.zukhridin.nework.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val userVM: UserViewModel by viewModels()

    @Inject
    lateinit var appAuth: AppAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bnvControl()
    }

    private fun bnvControl() {
        binding.bnv.itemIconTintList = null
        val navHostFragment =
            binding.navHostFragmentContainer.getFragment<Fragment>() as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(binding.bnv, navController)
        Glide.startWithBitmapCircleCrop(
            this,
            appAuth.authStateFlow.value?.avatar.toString(),
            binding.bnv.menu.findItem(R.id.profileFragment),
            resources
        )
    }
}
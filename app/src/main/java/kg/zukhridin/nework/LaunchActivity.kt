package kg.zukhridin.nework

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.dto.PostLikeOwnerIds
import kg.zukhridin.nework.entity.PostEntity
import kg.zukhridin.nework.viewmodel.PostViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {
    @Inject
    lateinit var appAuth: AppAuth
    private val postVM: PostViewModel by viewModels()

    @Inject
    lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        Handler(Looper.getMainLooper()).postDelayed({
            if (appAuth.authStateFlow.value?.token.isNullOrEmpty()) {
                val intent = Intent(this, ControlActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

                startActivity(intent)
                this.finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                this.finish()
            }
        }, 1000)

    }
}
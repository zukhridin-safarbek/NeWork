package kg.zukhridin.nework.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.R
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.data.util.Constants.YANDEX_MAP_KEY
import kg.zukhridin.nework.presentation.utils.Permissions
import javax.inject.Inject


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {
    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        println(appAuth.authStateFlow.value?.token)

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
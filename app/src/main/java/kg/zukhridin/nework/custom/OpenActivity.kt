package kg.zukhridin.nework.custom

import android.content.Intent
import androidx.fragment.app.FragmentActivity

class OpenActivity {
    companion object {
        fun startFromFragment(context: FragmentActivity, activity: Class<*>) {
            val intent = Intent(context.applicationContext, activity)
            context.startActivity(intent)
            context.finish()
        }
    }
}
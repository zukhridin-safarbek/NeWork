package kg.zukhridin.nework.custom

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kg.zukhridin.nework.MainActivity

class Glide {
    companion object {
        fun start(view: ImageView, url: String?) {
            Glide.with(view)
                .load(url ?: "https://hu.adat.one/wp-content/uploads/2021/05/jaj-ez-mi-nagy.jpg")
                .into(view)
        }

        fun startWithBitmapCircleCrop(view: MainActivity, url: String, item: MenuItem, resources: Resources) {
            val u = Uri.parse(url)
            Glide.with(view)
                .asBitmap()
                .load(u)
                .circleCrop()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?,
                    ) {
                        item.icon = BitmapDrawable(resources, resource)
                    }
                })
        }

        fun startWithBitmapCircleCrop(view: ImageView, url: String, item: MenuItem, resources: Resources) {
            Glide.with(view)
                .asBitmap()
                .load(url)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?,
                    ) {
                        item.icon = BitmapDrawable(resources, resource)
                    }
                })
        }
    }
}
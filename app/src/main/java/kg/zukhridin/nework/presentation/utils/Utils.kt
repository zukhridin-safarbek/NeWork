package kg.zukhridin.nework.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kg.zukhridin.nework.R
import kg.zukhridin.nework.presentation.activities.MainActivity

fun showShortToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun startWithBitmapCircleCrop(
    view: MainActivity,
    url: String,
    item: MenuItem,
    resources: Resources
) {
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

fun Fragment.startActivityFromFragment(context: FragmentActivity, activity: Class<*>) {
    val intent = Intent(context.applicationContext, activity)
    context.startActivity(intent)
    context.finish()
}

fun getCoordinationCountryNameLocalityThoroughfare(
    context: Context,
    lat: Double,
    lng: Double
): Map<String, String> {
    return try {
        val geo = Geocoder(context)
        val address = geo.getFromLocation(lat, lng, 1)
        return mapOf(
            "country" to "${address?.get(0)?.countryName}",
            "region" to "${address?.get(0)?.locality}",
            "thoroughfare" to "${address?.get(0)?.thoroughfare}"
        )
    } catch (e: Exception) {
        emptyMap()
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.changeHeaderBackground(url: String, view: ViewGroup) {
    Glide.with(this).load(url).override(15, 15).centerCrop()
        .into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                view.background = resource
            }

        })
}


fun showFullTextMoreBtn(btnMore: TextView, contentText: TextView, string: String) {
    btnMore.setOnClickListener {
        if (contentText.maxLines == 2) {
            contentText.maxLines = Integer.MAX_VALUE
            btnMore.text = btnMore.context.getString(R.string.less)
        } else {
            contentText.maxLines = 2
            btnMore.text = btnMore.context.getString(R.string.more)
        }
    }
    val lines = string.lines().size
    contentText.viewTreeObserver.addOnGlobalLayoutListener {
        if (lines > 2 || string.length > 100) {
            btnMore.visibility = View.VISIBLE
        } else {
            btnMore.visibility = View.GONE
        }
    }
}

fun avatarControl(image: ImageView, url: String) {
    Glide.with(image).load(url).fitCenter().circleCrop().into(image)
}

fun postSetAuthorNameToContent(
    content: String,
    author: String,
    assets: AssetManager
): SpannableStringBuilder {
    val ssb = SpannableStringBuilder()
    ssb.append("$author ")
    val authorDraw = object : ClickableSpan() {
        override fun onClick(widget: View) = Unit

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = Color.BLACK
            ds.typeface =
                Typeface.createFromAsset(assets, "fonts/philosopher_regular.ttf")
        }
    }
    ssb.append(content)
    ssb.setSpan(authorDraw, 0, author.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    return ssb
}

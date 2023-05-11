package kg.zukhridin.nework.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import kg.zukhridin.nework.model.ErrorResponseModel
import okhttp3.ResponseBody

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

fun getErrorBody(errorBody: ResponseBody?): ErrorResponseModel {
    val eb = errorBody?.string()
    val gson = Gson()
    return gson.fromJson(eb, ErrorResponseModel::class.java) ?: ErrorResponseModel(null)
}
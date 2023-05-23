package kg.zukhridin.nework.data.util

import com.google.gson.Gson
import kg.zukhridin.nework.domain.models.ErrorResponseModel
import okhttp3.ResponseBody

fun getErrorBody(errorBody: ResponseBody?): ErrorResponseModel {
    val eb = errorBody?.string()
    val gson = Gson()
    return gson.fromJson(eb, ErrorResponseModel::class.java) ?: ErrorResponseModel(null)
}
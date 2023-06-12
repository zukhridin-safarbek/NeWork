package kg.zukhridin.nework.data.util

import com.google.gson.Gson
import kg.zukhridin.nework.domain.models.ErrorResponseModel
import okhttp3.ResponseBody
import org.json.JSONObject

fun getErrorBody(errorBody: ResponseBody?): ErrorResponseModel {
    return if (errorBody != null) {
        val eb = errorBody.string()
        val result = Gson().fromJson(eb, ErrorResponseModel::class.java)
        ErrorResponseModel(reason = result.reason, content = result.content)
    } else {
        ErrorResponseModel()
    }
}
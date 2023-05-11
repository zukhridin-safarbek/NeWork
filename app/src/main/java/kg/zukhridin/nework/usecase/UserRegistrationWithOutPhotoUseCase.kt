package kg.zukhridin.nework.usecase

import com.google.gson.Gson
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.model.ErrorResponseModel
import kg.zukhridin.nework.service.APIService
import kg.zukhridin.nework.utils.getErrorBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody

class UserRegistrationWithOutPhotoUseCase(
    private val service: APIService,
    private val appAuth: AppAuth
) {
    suspend fun userRegistrationWithOutPhotoUseCase(
        login: String,
        password: String,
        name: String,
    ): Pair<Boolean, ErrorResponseModel?> {
        val pair: Pair<Boolean, ErrorResponseModel?>
        val response = service.registrationWithOutPhoto(
            login.toRequestBody("text/plain".toMediaType()),
            password.toRequestBody("text/plain".toMediaType()),
            name.toRequestBody("text/plain".toMediaType())
        )
        val id = response.body()?.id
        val token = response.body()?.token
        if (id != null && token != null) {
            appAuth.setAuth(
                id,
                token
            )
        }
        pair = Pair(response.isSuccessful, getErrorBody(response.errorBody()))
        return pair
    }
}
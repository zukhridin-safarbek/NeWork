package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.model.ErrorResponseModel
import kg.zukhridin.nework.model.PhotoModel
import kg.zukhridin.nework.service.APIService
import kg.zukhridin.nework.utils.getErrorBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRegistrationWithPhotoUseCase(private val service: APIService, private val appAuth: AppAuth) {
    suspend fun userRegistrationWithPhotoUseCase(
        login: String,
        password: String,
        name: String,
        photo: PhotoModel,
    ): Pair<Boolean, ErrorResponseModel?> {
        val pair: Pair<Boolean, ErrorResponseModel?>
        val response = service.registrationWithPhoto(
            login.toRequestBody("text/plain".toMediaType()),
            password.toRequestBody("text/plain".toMediaType()),
            name.toRequestBody("text/plain".toMediaType()),
            MultipartBody.Part.createFormData(
                "file",
                photo.file?.name,
                requireNotNull(photo.file?.asRequestBody())
            )
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
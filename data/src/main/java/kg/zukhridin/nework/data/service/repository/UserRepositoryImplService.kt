package kg.zukhridin.nework.data.service.repository

import kg.zukhridin.nework.data.service.requests.AuthAPIService
import kg.zukhridin.nework.data.service.requests.UserAPIService
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.data.util.getErrorBody
import kg.zukhridin.nework.domain.models.ErrorResponseModel
import kg.zukhridin.nework.domain.models.PhotoModel
import kg.zukhridin.nework.domain.models.User
import kg.zukhridin.nework.domain.service.repositories.UserRepositoryService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class UserRepositoryImplService @Inject constructor(
    private val userService: UserAPIService,
    private val authService: AuthAPIService,
    private val appAuth: AppAuth,
) : UserRepositoryService {
    private val users = mutableListOf<List<User>?>()

    override suspend fun userAuthentication(
        login: String,
        password: String
    ): Pair<Boolean, ErrorResponseModel?> {
        val response = authService.userAuthentication(login, password)
        if (response.body() != null) {
            appAuth.setAuth(
                response.body()!!.id,
                response.body()!!.token
            )
        }
        return if (response.isSuccessful) {
            Pair(response.isSuccessful, ErrorResponseModel(null))
        } else {
            Pair(response.isSuccessful, getErrorBody(response.errorBody()))
        }
    }


    override suspend fun getUsers(): List<User> {
        val response = userService.getUsers()
        return response.body().orEmpty()
    }

    override suspend fun getUserById(userId: Int): User? {
        return userService.getUserById(userId).body()
    }

    override suspend fun registrationWithPhoto(
        login: String,
        password: String,
        name: String,
        photo: PhotoModel,
    ): Pair<Boolean, ErrorResponseModel?> {
        val pair: Pair<Boolean, ErrorResponseModel?>
        val response = authService.registrationWithPhoto(
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

    override suspend fun registrationWithOutPhoto(
        login: String,
        password: String,
        name: String
    ): Pair<Boolean, ErrorResponseModel?> {
        val pair: Pair<Boolean, ErrorResponseModel?>
        val response = authService.registrationWithOutPhoto(
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
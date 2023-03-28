package kg.zukhridin.nework.repository.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.dto.Token
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.model.PhotoModel
import kg.zukhridin.nework.model.UserResponse
import kg.zukhridin.nework.repository.UserRepository
import kg.zukhridin.nework.service.APIService
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val service: APIService,
    private val appAuth: AppAuth,
) : UserRepository {
    private val _userResponseCode = MutableLiveData<ApiResult<List<User>?>>()
    private val _userAuthResponseCode = MutableLiveData<ApiResult<Token?>>()
    override val userResponseCode: MutableLiveData<ApiResult<List<User>?>>
        get() = _userResponseCode
    override val userAuthResponseCode: MutableLiveData<ApiResult<Token?>>
        get() = _userAuthResponseCode
    private val users = mutableListOf<List<User>?>()

    override suspend fun userLogIn(login: String, password: String) {
        val response = service.userLogIn(login, password)
        if (response.isSuccessful) {
            val id = response.body()?.id
            val token = response.body()?.token
            if (id != null && token != null) {
                users.map { list ->
                    list?.map { user ->
                        if (user.login == login) {
                            appAuth.setAuth(
                                id,
                                token,
                                user.name,
                                user.avatar ?: "null"
                            )
                            _userAuthResponseCode.value = ApiResult.Success(
                                response.code().toString(),
                                response.body()
                            )
                        }
                    }
                }


            }

        } else {
            _userAuthResponseCode.value =
                ApiResult.Error(
                    code = response.code().toString(),
                    message = response.message().toString(),
                )
        }
    }


    override suspend fun getUsers() {
        val response = service.getUsers()
        if (response.isSuccessful) {
            users.add(response.body())
            _userResponseCode.value = ApiResult.Success(
                code = response.code().toString(),
                data = response.body()
            )
        } else {
            _userResponseCode.value = ApiResult.Error(
                code = response.code().toString(),
                message = response.message()
            )
        }
    }

    override suspend fun userSignIn(
        login: String,
        password: String,
        name: String,
        photo: PhotoModel,
    ) {
        val response = service.userSignIn(
            login.toRequestBody("text/plain".toMediaType()),
            password.toRequestBody("text/plain".toMediaType()),
            name.toRequestBody("text/plain".toMediaType()),
            MultipartBody.Part.createFormData(
                "file",
                photo.file?.name,
                requireNotNull(photo.file?.asRequestBody())
            )
        )
        if (response.isSuccessful) {
            val id = response.body()?.id
            val token = response.body()?.token
            if (id != null && token != null) {
                userResponseCode.let { res ->
                    res.map { list ->
                        list.data?.map { user ->
                            if (user.login == login) {
                                appAuth.setAuth(
                                    id,
                                    token,
                                    user.name,
                                    user.avatar ?: "null"
                                )
                            }
                        }
                    }

                }

            }
            _userAuthResponseCode.value = (
                    ApiResult.Success(
                        response.code().toString(),
                        data = response.body()
                    )
                    )
        } else {
            _userAuthResponseCode.value = (
                    ApiResult.Error(
                        code = response.code().toString(),
                        message = response.message()
                    )
                    )
        }

    }
}
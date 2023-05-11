package kg.zukhridin.nework.repository.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.dto.Token
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.model.ErrorResponseModel
import kg.zukhridin.nework.model.PhotoModel
import kg.zukhridin.nework.repository.UserRepository
import kg.zukhridin.nework.service.APIService
import kg.zukhridin.nework.usecase.GetUserByIdFromServerUseCase
import kg.zukhridin.nework.usecase.UserRegistrationWithPhotoUseCase
import kg.zukhridin.nework.usecase.UserAuthenticationUseCase
import kg.zukhridin.nework.usecase.UserRegistrationWithOutPhotoUseCase
import okhttp3.MediaType.Companion.toMediaType
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

    override suspend fun userAuthentication(login: String, password: String): Pair<Boolean, ErrorResponseModel?> {
        return UserAuthenticationUseCase(service, appAuth).userAuthentication(login, password)
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

    override suspend fun getUserById(userId: Int): User? {
        return GetUserByIdFromServerUseCase(service).getUserByIdFromServerUseCase(userId)
    }

    override suspend fun registrationWithPhoto(
        login: String,
        password: String,
        name: String,
        photo: PhotoModel,
    ): Pair<Boolean, ErrorResponseModel?> {
        return UserRegistrationWithPhotoUseCase(service, appAuth).userRegistrationWithPhotoUseCase(
            login,
            password,
            name,
            photo
        )
    }

    override suspend fun registrationWithOutPhoto(login: String, password: String, name: String): Pair<Boolean, ErrorResponseModel?> {
        return UserRegistrationWithOutPhotoUseCase(service, appAuth).userRegistrationWithOutPhotoUseCase(login, password, name)
    }
}
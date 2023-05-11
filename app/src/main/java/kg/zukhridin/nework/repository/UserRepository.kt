package kg.zukhridin.nework.repository

import androidx.lifecycle.MutableLiveData
import kg.zukhridin.nework.dto.Token
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.model.ErrorResponseModel
import kg.zukhridin.nework.model.PhotoModel

interface UserRepository {
    val userResponseCode: MutableLiveData<ApiResult<List<User>?>>
    val userAuthResponseCode: MutableLiveData<ApiResult<Token?>>
    suspend fun userAuthentication(login: String, password: String): Pair<Boolean, ErrorResponseModel?>
    suspend fun getUsers()
    suspend fun getUserById(userId: Int): User?

    suspend fun registrationWithPhoto(
        login: String,
        password: String,
        name: String,
        photo: PhotoModel,
    ): Pair<Boolean, ErrorResponseModel?>

    suspend fun registrationWithOutPhoto(
        login: String,
        password: String,
        name: String,
    ): Pair<Boolean, ErrorResponseModel?>
}
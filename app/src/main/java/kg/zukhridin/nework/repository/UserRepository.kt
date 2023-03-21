package kg.zukhridin.nework.repository

import androidx.lifecycle.MutableLiveData
import kg.zukhridin.nework.dto.Token
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.model.PhotoModel
import kg.zukhridin.nework.model.UserResponse
import kotlinx.coroutines.flow.SharedFlow

interface UserRepository {
    val userResponseCode: MutableLiveData<ApiResult<List<User>?>>
    val userAuthResponseCode: MutableLiveData<ApiResult<Token?>>
    suspend fun userLogIn(login: String, password: String)
    suspend fun getUsers()

    suspend fun userSignIn(
        login: String,
        password: String,
        name: String,
        photo: PhotoModel,
    )
}
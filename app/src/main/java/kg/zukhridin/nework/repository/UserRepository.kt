package kg.zukhridin.nework.repository

import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.model.ResponseModel
import kotlinx.coroutines.flow.SharedFlow

interface UserRepository {
    val responseCode: SharedFlow<ResponseModel>
    suspend fun signIn(login: String, password: String)
    suspend fun getUsers(): List<User>?
}
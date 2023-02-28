package kg.zukhridin.nework.repository.impl

import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.model.ResponseModel
import kg.zukhridin.nework.repository.UserRepository
import kg.zukhridin.nework.service.APIService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import kotlin.math.log

class UserRepositoryImpl @Inject constructor(
    private val service: APIService,
    private val appAuth: AppAuth,
) : UserRepository {
    private val _responseCode = MutableSharedFlow<ResponseModel>()
    override val responseCode: SharedFlow<ResponseModel> = _responseCode
    override suspend fun signIn(login: String, password: String) {
        val response = service.signIn(login, password)
        if (response.isSuccessful) {
            val id = response.body()?.id
            val token = response.body()?.token
            if (id != null && token != null) {
                appAuth.setAuth(
                    id,
                    token,
                    appAuth.authStateFlow.value?.name ?: "name",
                    appAuth.authStateFlow.value?.avatar ?: "avatar is null"
                )
            } else {
                error(response.message())
            }
            _responseCode.emit(ResponseModel(response.code(), response.message()))
        }

    }

    override suspend fun getUsers(): List<User>? {
        val response = service.getUsers()
        return if (response.isSuccessful) {
            response.body()
        } else {
            error(response.message())
        }
    }
}
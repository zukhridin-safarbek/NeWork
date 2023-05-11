package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.model.ErrorResponseModel
import kg.zukhridin.nework.service.APIService
import kg.zukhridin.nework.utils.getErrorBody

class UserAuthenticationUseCase(private val service: APIService, private val appAuth: AppAuth) {
    suspend fun userAuthentication(
        login: String,
        password: String,
    ): Pair<Boolean, ErrorResponseModel?> {
        val pair: Pair<Boolean, ErrorResponseModel?>
        val response = service.userAuthentication(login, password)
        if (response.body() != null) {
            appAuth.setAuth(
                response.body()!!.id,
                response.body()!!.token
            )
        }
        pair = Pair(response.isSuccessful, getErrorBody(response.errorBody()))
        return pair
    }
}
package kg.zukhridin.nework.data.service.requests

import kg.zukhridin.nework.domain.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserAPIService {
    @GET("users/")
    suspend fun getUsers(): Response<List<User>>

    @GET("users/{user_id}/")
    suspend fun getUserById(@Path("user_id") userId: Int): Response<User>
}
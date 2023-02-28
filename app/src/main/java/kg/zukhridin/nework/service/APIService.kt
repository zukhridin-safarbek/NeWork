package kg.zukhridin.nework.service

import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.dto.UserAuthDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface APIService {
    @GET("users/")
    suspend fun getUsers(): Response<List<User>>

    @FormUrlEncoded
    @POST("users/authentication/")
    suspend fun signIn(
        @Field("login") login: String,
        @Field("password") password: String,
    ): Response<UserAuthDto>
}
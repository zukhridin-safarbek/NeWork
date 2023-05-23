package kg.zukhridin.nework.data.service.requests

import kg.zukhridin.nework.domain.models.Token
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AuthAPIService {
    @FormUrlEncoded
    @POST("users/authentication/")
    suspend fun userAuthentication(
        @Field("login") login: String,
        @Field("password") password: String,
    ): Response<Token>

    @Multipart
    @POST("users/registration/")
    suspend fun registrationWithPhoto(
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part,
    ): Response<Token>

    @Multipart
    @POST("users/registration/")
    suspend fun registrationWithOutPhoto(
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody
    ): Response<Token>
}
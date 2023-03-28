package kg.zukhridin.nework.service

import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.dto.Token
import kg.zukhridin.nework.entity.PostEntity
import kg.zukhridin.nework.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface APIService {
    @GET("users/")
    suspend fun getUsers(): Response<List<User>>

    @GET("posts/")
    suspend fun getPosts(): Response<List<Post>>

    @FormUrlEncoded
    @POST("users/authentication/")
    suspend fun userLogIn(
        @Field("login") login: String,
        @Field("password") password: String,
    ): Response<Token>

    @Multipart
    @POST("users/registration/")
    suspend fun userSignIn(
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part,
    ): Response<Token>

    @GET("posts/latest/")
    suspend fun getLatestPosts(@Query("count") count: Int): Response<List<PostEntity>>

    @GET("posts/{post_id}/before/")
    suspend fun getBeforePosts(
        @Path("post_id") id: Int,
        @Query("count") count: Int
    ): Response<List<PostEntity>>

    @GET("posts/{post_id}/after/")
    suspend fun getAfterPosts(
        @Path("post_id") id: Int,
        @Query("count") count: Int
    ): Response<List<PostEntity>>

    @POST("posts/{post_id}/likes/")
    suspend fun likeById(
        @Path("post_id") id: Int,
    ): Response<Post>

    @DELETE("posts/{post_id}/likes")
    suspend fun dislikeById(
        @Path("post_id") id: Int,
    ): Response<Post>

    @POST("posts/")
    suspend fun insertPost(@Body post: Post): Response<Unit>

    @GET("{author_id}/wall/")
    suspend fun getWalls(@Path("author_id") author_id: Int): Response<List<Post>>
}
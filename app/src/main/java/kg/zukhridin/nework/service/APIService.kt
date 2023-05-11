package kg.zukhridin.nework.service

import kg.zukhridin.nework.dto.*
import kg.zukhridin.nework.entity.EventEntity
import kg.zukhridin.nework.entity.JobEntity
import kg.zukhridin.nework.entity.PostEntity
import kg.zukhridin.nework.model.MediaResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface APIService {
    @GET("users/")
    suspend fun getUsers(): Response<List<User>>

    @FormUrlEncoded
    @POST("users/authentication/")
    suspend fun userAuthentication(
        @Field("login") login: String,
        @Field("password") password: String,
    ): Response<Token>

    @GET("users/{user_id}/")
    suspend fun getUserById(@Path("user_id") userId: Int): Response<User>

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

    @GET("events/latest/")
    suspend fun getLatestEvents(@Query("count") count: Int): Response<List<EventEntity>>

    @GET("events/{event_id}/after/")
    suspend fun getAfterEvents(
        @Query("count") count: Int,
        @Path("event_id") eventId: Int
    ): Response<List<EventEntity>>

    @GET("events/{event_id}/before/")
    suspend fun getBeforeEvents(
        @Query("count") count: Int,
        @Path("event_id") eventId: Int
    ): Response<List<EventEntity>>

    @POST("posts/{post_id}/likes/")
    suspend fun likeById(
        @Path("post_id") id: Int,
    ): Response<Post>

    @DELETE("posts/{post_id}/likes")
    suspend fun dislikeById(
        @Path("post_id") id: Int,
    ): Response<Post>

    @Multipart
    @POST("media/")
    suspend fun insertMedia(@Part file: MultipartBody.Part): Response<MediaResponse>

    @POST("posts/")
    suspend fun insertPost(@Body post: Post): Response<Unit>

    @POST("posts/")
    suspend fun updatePostById(@Body post: Post): Response<Unit>

    @DELETE("posts/{post_id}/")
    suspend fun deletePostById(@Path("post_id") id: Int): Response<Unit>

    @GET("{author_id}/wall/")
    suspend fun getWalls(@Path("author_id") author_id: Int): Response<List<Post>>

    @GET("my/jobs/")
    suspend fun getMyJobs(): Response<List<JobEntity>>

    @GET("{user_id}/jobs/")
    suspend fun getUserJobs(@Path("user_id") userId: Int): Response<List<JobEntity>>

    @GET("posts/{post_id}/")
    suspend fun getPostById(@Path("post_id") postId: Int): Response<Post>

    @POST("my/jobs/")
    suspend fun insertJob(@Body job: Job): Response<Job>

    @DELETE("my/jobs/{job_id}/")
    suspend fun deleteJob(@Path("job_id") jobId: Int): Response<Any>

}
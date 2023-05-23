package kg.zukhridin.nework.data.service.requests

import kg.zukhridin.nework.data.entity.PostEntity
import kg.zukhridin.nework.domain.models.Post
import retrofit2.Response
import retrofit2.http.*


interface PostAPIService {
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

    @GET("posts/{post_id}/")
    suspend fun getPostById(@Path("post_id") postId: Int): Response<Post>

    @POST("posts/")
    suspend fun insertPost(@Body post: Post): Response<Unit>

    @POST("posts/")
    suspend fun updatePostById(@Body post: Post): Response<Unit>

    @DELETE("posts/{post_id}/")
    suspend fun deletePostById(@Path("post_id") id: Int): Response<Unit>

    @POST("posts/{post_id}/likes/")
    suspend fun likeById(
        @Path("post_id") id: Int,
    ): Response<Post>

    @DELETE("posts/{post_id}/likes")
    suspend fun dislikeById(
        @Path("post_id") id: Int,
    ): Response<Post>
}
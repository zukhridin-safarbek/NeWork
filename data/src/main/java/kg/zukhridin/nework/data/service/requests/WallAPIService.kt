package kg.zukhridin.nework.data.service.requests

import kg.zukhridin.nework.data.entity.PostEntity
import kg.zukhridin.nework.data.entity.WallEntity
import kg.zukhridin.nework.domain.models.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WallAPIService {
    @GET("{author_id}/wall/")
    suspend fun getWalls(@Path("author_id") author_id: Int): Response<List<WallEntity>>
    @GET("my/wall/latest/")
    suspend fun getLatestPosts(@Query("count") count: Int): Response<List<WallEntity>>

    @GET("my/wall/{post_id}/before/")
    suspend fun getBeforePosts(
        @Path("post_id") id: Int,
        @Query("count") count: Int
    ): Response<List<WallEntity>>

    @GET("my/wall/{post_id}/after/")
    suspend fun getAfterPosts(
        @Path("post_id") id: Int,
        @Query("count") count: Int
    ): Response<List<WallEntity>>
}
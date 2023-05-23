package kg.zukhridin.nework.data.service.requests

import kg.zukhridin.nework.domain.models.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WallAPIService {
    @GET("{author_id}/wall/")
    suspend fun getWalls(@Path("author_id") author_id: Int): Response<List<Post>>
}
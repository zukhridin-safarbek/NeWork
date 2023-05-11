package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.service.APIService

class GetPostByIdFromServerUseCase(private val service: APIService) {
    suspend fun getPostByIdFromServer(postId: Int): Post {
        val response = service.getPostById(postId)
        return response.body()!!
    }
}
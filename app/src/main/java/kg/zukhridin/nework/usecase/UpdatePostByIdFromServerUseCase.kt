package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.service.APIService

class UpdatePostByIdFromServerUseCase(private val service: APIService) {
    suspend fun updatePostByIdFromServer(post: Post){
        if (post.id != 0) {
            service.updatePostById(post)
        }
    }
}
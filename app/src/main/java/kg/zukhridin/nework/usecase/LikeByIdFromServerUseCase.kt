package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.service.APIService

class LikeByIdFromServerUseCase(private val service: APIService) {
    suspend fun likeByIdFromServer(post: Post){
        if (!post.likedByMe) {
            service.likeById(post.id)
        } else {
            service.dislikeById(post.id)
        }
    }
}
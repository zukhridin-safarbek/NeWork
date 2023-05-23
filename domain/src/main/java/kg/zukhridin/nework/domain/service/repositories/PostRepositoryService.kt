package kg.zukhridin.nework.domain.service.repositories

import kg.zukhridin.nework.domain.models.Post

interface PostRepositoryService {
    suspend fun likeById(post: Post)
    suspend fun dislikeById(post: Post)
    suspend fun getWall(userId: Int): Map<Int, List<Post>>
    suspend fun insertPost(post: Post): Boolean
    suspend fun deletePostById(id: Int)
    suspend fun updatePostById(post: Post)
}
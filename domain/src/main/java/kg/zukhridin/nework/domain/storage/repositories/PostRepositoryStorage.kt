package kg.zukhridin.nework.domain.storage.repositories

import androidx.paging.PagingData
import kg.zukhridin.nework.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface PostRepositoryStorage {
    val data: Flow<PagingData<Post>>
    suspend fun getPostById(postId: Int): Post
    suspend fun insertPost(post: Post)
    suspend fun updatePost(post: Post)
    suspend fun likeById(post: Post)
    suspend fun dislikeById(post: Post)
    suspend fun deletePostById(id: Int)
    suspend fun clearAllPosts()
}
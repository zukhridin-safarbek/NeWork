package kg.zukhridin.nework.repository

import androidx.paging.PagingData
import kg.zukhridin.nework.dto.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val data: Flow<PagingData<Post>>
    suspend fun getPosts(): List<Post>?
    suspend fun likeById(post: Post)
    suspend fun insertPostToService(post: Post)
}
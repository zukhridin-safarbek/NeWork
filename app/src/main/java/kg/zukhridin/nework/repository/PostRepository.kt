package kg.zukhridin.nework.repository

import android.content.Context
import androidx.paging.PagingData
import kg.zukhridin.nework.dto.CustomMedia
import kg.zukhridin.nework.dto.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val data: Flow<PagingData<Post>>
    suspend fun likeById(post: Post)
    suspend fun getPostById(postId: Int): Post
    suspend fun getWall(userId: Int): Map<Int, List<Post>>
    suspend fun insertPostToService(post: Post): Boolean
    suspend fun deletePostByIdFromServer(id: Int)
    suspend fun deletePostByIdFromExternalStorage(id: Int)
    suspend fun updatePostByIdFromServer(post: Post)
    suspend fun updatePostByIdFromExternalStorage(post: Post)
    suspend fun getImagesFromGallery(context: Context): ArrayList<CustomMedia>
    suspend fun getVideosFromGallery(context: Context): ArrayList<CustomMedia>
    suspend fun getAudiosFromGallery(context: Context): ArrayList<CustomMedia>
}
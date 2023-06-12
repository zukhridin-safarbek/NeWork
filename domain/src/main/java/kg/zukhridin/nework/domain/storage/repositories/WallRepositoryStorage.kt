package kg.zukhridin.nework.domain.storage.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kg.zukhridin.nework.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface WallRepositoryStorage {
    val data: Flow<PagingData<Post>>
    suspend fun insertWall(wall: Post)
    suspend fun getWallById(wallId: Int): Post
    suspend fun getWallsByUserId(userId: Int): LiveData<List<Post>?>
}
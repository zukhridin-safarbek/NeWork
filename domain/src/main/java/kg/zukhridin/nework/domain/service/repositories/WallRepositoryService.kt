package kg.zukhridin.nework.domain.service.repositories

import androidx.lifecycle.LiveData
import kg.zukhridin.nework.domain.models.Post

interface WallRepositoryService {
    suspend fun getWallsByUserId(userId: Int): LiveData<List<Post>>
}
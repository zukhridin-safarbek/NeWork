package kg.zukhridin.nework.data.storage.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kg.zukhridin.nework.data.entity.WallEntity
import kg.zukhridin.nework.data.paging.WallRemoteMediator
import kg.zukhridin.nework.data.service.requests.WallAPIService
import kg.zukhridin.nework.data.storage.dao.WallDao
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.domain.storage.repositories.WallRepositoryStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WallRepositoryImplStorage @Inject constructor(
    private val wallDao: WallDao,
    private val service: WallAPIService
): WallRepositoryStorage {
    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 5, enablePlaceholders = false),
        pagingSourceFactory = {
            wallDao.getWallsPagingSource()
        },
        remoteMediator = WallRemoteMediator(service = service, wallDao = wallDao)
    ).flow.map {
        it.map(WallEntity::toDto)
    }

    override suspend fun insertWall(wall: Post) {
        wallDao.insertWall(WallEntity.fromDto(wall))
    }

    override suspend fun getWallById(wallId: Int): Post {
        return wallDao.getWallById(wallId).toDto()
    }

    override suspend fun getWallsByUserId(userId: Int): LiveData<List<Post>?> {
        val liveData : MutableLiveData<List<Post>?> = MutableLiveData()
        val wall = wallDao.getWallsByUserId(userId).value?.map(WallEntity::toDto)
        liveData.postValue(wall)
        return liveData
    }
}
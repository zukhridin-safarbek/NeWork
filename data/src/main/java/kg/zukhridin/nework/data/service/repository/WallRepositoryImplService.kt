package kg.zukhridin.nework.data.service.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kg.zukhridin.nework.data.entity.WallEntity
import kg.zukhridin.nework.data.service.requests.WallAPIService
import kg.zukhridin.nework.data.storage.dao.WallDao
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.domain.service.repositories.WallRepositoryService
import javax.inject.Inject

class WallRepositoryImplService @Inject constructor(private val wallAPIService: WallAPIService,
                                                    private val wallDao: WallDao) :
    WallRepositoryService {
    override suspend fun getWallsByUserId(userId: Int): LiveData<List<Post>> {
        val liveData = MutableLiveData<List<Post>>()
        val data = wallAPIService.getWalls(userId).body().orEmpty()
        wallDao.insertWalls(data)
        liveData.postValue(data.map(WallEntity::toDto))
        return liveData
    }
}
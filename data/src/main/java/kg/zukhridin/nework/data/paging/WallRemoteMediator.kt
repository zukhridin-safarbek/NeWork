package kg.zukhridin.nework.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kg.zukhridin.nework.data.entity.WallEntity
import kg.zukhridin.nework.data.service.requests.WallAPIService
import kg.zukhridin.nework.data.storage.dao.WallDao

@OptIn(ExperimentalPagingApi::class)
class WallRemoteMediator(private val service: WallAPIService,
                         private val wallDao: WallDao):RemoteMediator<Int, WallEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WallEntity>
    ): MediatorResult {
        try {
            val result = when (loadType) {
                LoadType.REFRESH -> {
                    service.getLatestPosts(state.config.initialLoadSize)
                }
                LoadType.PREPEND -> {
                    val id = state.firstItemOrNull()?.id ?: return MediatorResult.Success(false)
                    service.getAfterPosts(id, state.config.initialLoadSize)
                }
                LoadType.APPEND -> {
                    val id = state.lastItemOrNull()?.id ?: return MediatorResult.Success(false)
                    service.getBeforePosts(id, state.config.initialLoadSize)
                }
            }
            val data = result.body().orEmpty()
            wallDao.insertWalls(data)
            return MediatorResult.Success(data.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
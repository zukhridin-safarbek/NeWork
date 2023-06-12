package kg.zukhridin.nework.data.paging

import android.util.Log
import androidx.paging.*
import kg.zukhridin.nework.data.storage.dao.PostDao
import kg.zukhridin.nework.data.service.requests.PostAPIService
import kg.zukhridin.nework.data.entity.PostEntity
import kg.zukhridin.nework.data.util.Constants.MY_LOG

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val service: PostAPIService,
    private val postDao: PostDao,
) :
    RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>,
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
            postDao.insertPosts(data)
            return MediatorResult.Success(data.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
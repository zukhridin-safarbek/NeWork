package kg.zukhridin.nework.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kg.zukhridin.nework.data.entity.EventEntity
import kg.zukhridin.nework.data.service.requests.EventAPIService
import kg.zukhridin.nework.data.storage.dao.EventDao

@OptIn(ExperimentalPagingApi::class)
class EventRemoteMediator(private val service: EventAPIService, private val eventDao: EventDao) :
    RemoteMediator<Int, EventEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventEntity>
    ): MediatorResult {
        try {
            val result = when (loadType) {
                REFRESH -> service.getLatestEvents(state.config.initialLoadSize)
                PREPEND -> {
                    val id = state.firstItemOrNull()?.id ?: return MediatorResult.Success(false)
                    service.getAfterEvents(id, state.config.initialLoadSize)
                }
                APPEND -> {
                    val id = state.lastItemOrNull()?.id ?: return MediatorResult.Success(false)
                    service.getBeforeEvents(id, state.config.initialLoadSize)
                }
            }
            val data = result.body().orEmpty()
            try {
                eventDao.insertEvents(data)
            }catch (e: Exception){
                e.printStackTrace()
            }
            return MediatorResult.Success(data.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
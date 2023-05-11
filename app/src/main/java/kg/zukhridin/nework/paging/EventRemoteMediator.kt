package kg.zukhridin.nework.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kg.zukhridin.nework.database.dao.EventDao
import kg.zukhridin.nework.dto.EventDto
import kg.zukhridin.nework.entity.EventEntity
import kg.zukhridin.nework.entity.PostEntity
import kg.zukhridin.nework.service.APIService

@OptIn(ExperimentalPagingApi::class)
class EventRemoteMediator(private val service: APIService, private val eventDao: EventDao) :
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
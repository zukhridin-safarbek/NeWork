package kg.zukhridin.nework.repository.impl

import androidx.paging.*
import kg.zukhridin.nework.database.dao.EventDao
import kg.zukhridin.nework.dto.EventDto
import kg.zukhridin.nework.entity.EventEntity
import kg.zukhridin.nework.paging.EventRemoteMediator
import kg.zukhridin.nework.repository.EventRepository
import kg.zukhridin.nework.service.APIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EventRepositoryImpl @Inject constructor(
    private val service: APIService,
    private val eventDao: EventDao
) :
    EventRepository {
    override val data: Flow<PagingData<EventDto>> = Pager(
        config = PagingConfig(pageSize = 5, enablePlaceholders = false),
        pagingSourceFactory = { eventDao.getEventsPagingSource() },
        remoteMediator = EventRemoteMediator(service = service, eventDao),
    ).flow.map {
        it.map(EventEntity::toDto)
    }

    override suspend fun getData(): Flow<List<EventEntity>> {
        return eventDao.getData()
    }
}
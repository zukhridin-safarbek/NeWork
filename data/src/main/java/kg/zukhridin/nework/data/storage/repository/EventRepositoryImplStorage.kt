package kg.zukhridin.nework.data.storage.repository

import androidx.paging.*
import kg.zukhridin.nework.data.entity.EventEntity
import kg.zukhridin.nework.data.paging.EventRemoteMediator
import kg.zukhridin.nework.data.service.requests.EventAPIService
import kg.zukhridin.nework.data.storage.dao.EventDao
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.domain.storage.repositories.EventRepositoryStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EventRepositoryImplStorage @Inject constructor(
    eventAPIService: EventAPIService,
    private val eventDao: EventDao,
    private val appAuth: AppAuth
) :
    EventRepositoryStorage {
    override val data: Flow<PagingData<Event>> = Pager(
        config = PagingConfig(pageSize = 5, enablePlaceholders = false),
        pagingSourceFactory = { eventDao.getEventsPagingSource() },
        remoteMediator = EventRemoteMediator(service = eventAPIService, eventDao),
    ).flow.map { value ->
        value.map(EventEntity::toDto)
    }

    override suspend fun getData(): Flow<List<Event>> {
        return eventDao.getData().map { value -> value.map(EventEntity::toDto) }
    }

    override suspend fun clearAllEvents() {
        eventDao.clearAllEvent()
    }

    override suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(EventEntity.fromDto(event))
    }

    override suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(eventId = event.id)
    }

    override suspend fun eventLikeById(event: Event) {
        val list = event.likeOwnerIds.toMutableList()
        list.add(appAuth.authStateFlow.value?.id!!)
        eventDao.eventLikeById(
            EventEntity.fromDto(event).copy(likedByMe = true, likeOwnerIds = list)
        )
    }

    override suspend fun eventDislikeById(event: Event) {
        val list = event.likeOwnerIds.toMutableList()
        list.remove(appAuth.authStateFlow.value?.id)
        eventDao.eventLikeById(
            EventEntity.fromDto(event).copy(likedByMe = false, likeOwnerIds = list)
        )
    }
}
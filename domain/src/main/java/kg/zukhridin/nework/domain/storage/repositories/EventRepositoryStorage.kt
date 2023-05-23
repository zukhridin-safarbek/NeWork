package kg.zukhridin.nework.domain.storage.repositories

import androidx.paging.PagingData
import kg.zukhridin.nework.domain.models.Event
import kotlinx.coroutines.flow.Flow

interface EventRepositoryStorage {
    val data: Flow<PagingData<Event>>
    suspend fun getData(): Flow<List<Event>>
    suspend fun clearAllEvents()
    suspend fun eventLikeById(event: Event)
    suspend fun insertEvent(event: Event)
    suspend fun deleteEvent(event: Event)
    suspend fun eventDislikeById(event: Event)

}
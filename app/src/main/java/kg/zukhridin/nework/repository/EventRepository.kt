package kg.zukhridin.nework.repository

import androidx.paging.PagingData
import kg.zukhridin.nework.dto.EventDto
import kg.zukhridin.nework.entity.EventEntity
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    val data: Flow<PagingData<EventDto>>
    suspend fun getData(): Flow<List<EventEntity>>

}
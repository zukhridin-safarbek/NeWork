package kg.zukhridin.nework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.domain.models.ErrorResponseModel
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.domain.service.repositories.EventRepositoryService
import kg.zukhridin.nework.domain.storage.repositories.EventRepositoryStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repositoryService: EventRepositoryService,
    private val repositoryStorage: EventRepositoryStorage
) : ViewModel() {
    val data: Flow<PagingData<Event>> = repositoryStorage.data.flowOn(Dispatchers.Default)

    suspend fun clearAllEvents() {
        repositoryStorage.clearAllEvents()
    }

    fun eventLikeById(event: Event)= viewModelScope.launch {
        if (event.likedByMe) {
            repositoryStorage.eventDislikeById(event)
            repositoryService.eventDislikeById(event)
        } else {
            repositoryStorage.eventLikeById(event)
            repositoryService.eventLikeById(event)
        }
    }

    suspend fun insertEvent(event: Event): Pair<Boolean, ErrorResponseModel?> = withContext(
        coroutineContext
    ) {
        val response = repositoryService.insertEvent(event)
        response
    }

    suspend fun deleteEvent(event: Event): Boolean = withContext(viewModelScope.coroutineContext) {
        repositoryStorage.deleteEvent(event)
        repositoryService.deleteEvent(event)
    }
}
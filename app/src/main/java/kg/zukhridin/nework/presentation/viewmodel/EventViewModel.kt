package kg.zukhridin.nework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _responseIsSuccessFull = MutableLiveData<Boolean?>()
    val responseIsSuccessFull: LiveData<Boolean?> = _responseIsSuccessFull
    private val _responseReason = MutableLiveData<ErrorResponseModel?>()
    val responseReason: LiveData<ErrorResponseModel?> = _responseReason
    fun eventLikeById(event: Event)= viewModelScope.launch {
        if (event.likedByMe) {
            repositoryStorage.eventDislikeById(event)
            repositoryService.eventDislikeById(event)
        } else {
            repositoryStorage.eventLikeById(event)
            repositoryService.eventLikeById(event)
        }
    }

    fun insertEvent(event: Event) = viewModelScope.launch {
        val response = repositoryService.insertEvent(event)
        _responseIsSuccessFull.postValue(response.first)
        _responseReason.postValue(response.second)
    }

    suspend fun deleteEvent(event: Event): Boolean = withContext(viewModelScope.coroutineContext) {
        repositoryStorage.deleteEvent(event)
        repositoryService.deleteEvent(event)
    }
}
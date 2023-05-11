package kg.zukhridin.nework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.entity.EventEntity
import kg.zukhridin.nework.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val repository: EventRepository) : ViewModel() {
    val data = repository.data.flowOn(Dispatchers.Default)
    suspend fun getEvent(): Flow<List<EventEntity>> = withContext(viewModelScope.coroutineContext) {
        repository.getData()
    }
}
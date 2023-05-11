package kg.zukhridin.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.dto.Job
import kg.zukhridin.nework.repository.JobRepository
import kg.zukhridin.nework.usecase.GetMyJobsFromServerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(private val repository: JobRepository) : ViewModel() {
    suspend fun getMyJobs(): Pair<Boolean, Flow<List<Job>>> =
        withContext(viewModelScope.coroutineContext) {
            repository.getMyJobs()
        }

    suspend fun createJob(job: Job): Boolean = withContext(viewModelScope.coroutineContext) {
        repository.createJob(job)
    }

    suspend fun getUserJobs(userId: Int): Pair<Boolean, Flow<List<Job>>> =
        withContext(viewModelScope.coroutineContext) {
            repository.getUserJobs(userId)
        }

    suspend fun deleteMyJob(job: Job) = viewModelScope.launch {
        repository.deleteJob(job)
    }
}
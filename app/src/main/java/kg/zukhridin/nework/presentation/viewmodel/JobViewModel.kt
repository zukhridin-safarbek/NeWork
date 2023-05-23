package kg.zukhridin.nework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.domain.models.Job
import kg.zukhridin.nework.domain.service.repositories.JobRepositoryService
import kg.zukhridin.nework.domain.storage.repositories.JobRepositoryStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val repositoryService: JobRepositoryService,
    private val repositoryStorage: JobRepositoryStorage
) : ViewModel() {
    suspend fun createJob(job: Job): Boolean = withContext(viewModelScope.coroutineContext) {
        repositoryStorage.insertJob(job)
        repositoryService.createJob(job)
    }

    suspend fun getUserJobs(userId: Int): Pair<Boolean, Flow<List<Job>>> =
        withContext(viewModelScope.coroutineContext) {
            repositoryStorage.clearJobs()
            val jobsFromService = repositoryService.getUserJobs(userId)
            repositoryStorage.insertJobs(jobs = jobsFromService.second.first())
            repositoryStorage.getUserJobs(userId)
        }

    suspend fun deleteMyJob(job: Job) = viewModelScope.launch {
        repositoryStorage.deleteJob(job)
        repositoryService.deleteJob(job)
    }
}
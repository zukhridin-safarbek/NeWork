package kg.zukhridin.nework.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import kg.zukhridin.nework.dto.Job
import kg.zukhridin.nework.entity.JobEntity
import kg.zukhridin.nework.service.APIService

class GetMyJobsFromServerUseCase(private val service: APIService) {
    suspend fun getMyJobsFromServerUseCase(): Pair<Boolean, LiveData<List<JobEntity>>> {
        val response = service.getMyJobs()
        val jobs = MutableLiveData(response.body().orEmpty())
        return Pair(response.isSuccessful, jobs)
    }
}
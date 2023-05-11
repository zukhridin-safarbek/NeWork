package kg.zukhridin.nework.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kg.zukhridin.nework.dto.Job
import kg.zukhridin.nework.entity.JobEntity
import kg.zukhridin.nework.service.APIService

class GetUsersJobUseCase(private val service: APIService) {
    suspend fun getUsersJobUseCase(userId: Int): Pair<Boolean, LiveData<List<JobEntity>>> {
        val response = service.getUserJobs(userId)
        val jobs = MutableLiveData(response.body().orEmpty())
        return Pair(response.isSuccessful, jobs)
    }
}
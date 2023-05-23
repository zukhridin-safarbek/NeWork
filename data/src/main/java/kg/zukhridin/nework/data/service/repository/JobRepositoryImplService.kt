package kg.zukhridin.nework.data.service.repository

import kg.zukhridin.nework.data.service.requests.JobAPIService
import kg.zukhridin.nework.domain.models.Job
import kg.zukhridin.nework.domain.service.repositories.JobRepositoryService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class JobRepositoryImplService @Inject constructor(
    private val service: JobAPIService,
) : JobRepositoryService {
    override suspend fun createJob(job: Job): Boolean {
        val response = service.insertJob(job)
        return response.isSuccessful
    }
    override suspend fun deleteJob(job: Job): Boolean {
        val response = service.deleteJob(job.id)
        return response.isSuccessful
    }
    override suspend fun getUserJobs(userId: Int): Pair<Boolean, Flow<List<Job>>> {
        val response = service.getUserJobs(userId)
        return Pair(response.isSuccessful, flow { response.body().orEmpty() })
    }
}
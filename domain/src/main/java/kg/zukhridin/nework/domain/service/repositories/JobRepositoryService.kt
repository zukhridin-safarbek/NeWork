package kg.zukhridin.nework.domain.service.repositories

import kg.zukhridin.nework.domain.models.Job
import kotlinx.coroutines.flow.Flow

interface JobRepositoryService {
    suspend fun createJob(job: Job): Boolean
    suspend fun deleteJob(job: Job): Boolean
    suspend fun getUserJobs(userId: Int): Pair<Boolean, Flow<List<Job>>>
}
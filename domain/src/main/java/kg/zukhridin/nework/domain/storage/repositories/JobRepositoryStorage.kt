package kg.zukhridin.nework.domain.storage.repositories

import kg.zukhridin.nework.domain.models.Job
import kotlinx.coroutines.flow.Flow

interface JobRepositoryStorage {
    suspend fun insertJob(job: Job, userId: Int): Boolean
    suspend fun deleteJob(job: Job): Boolean
    suspend fun getUserJobs(userId: Int): Pair<Boolean, Flow<List<Job>>>
    suspend fun clearJobs(): Int
    suspend fun insertJobs(jobs: List<Job>, userId: Int)
}
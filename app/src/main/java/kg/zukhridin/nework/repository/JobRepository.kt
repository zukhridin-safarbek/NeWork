package kg.zukhridin.nework.repository

import androidx.lifecycle.LiveData
import kg.zukhridin.nework.dto.Job
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    suspend fun getMyJobs(): Pair<Boolean, Flow<List<Job>>>
    suspend fun createJob(job: Job): Boolean
    suspend fun deleteJob(job: Job): Boolean
    suspend fun getUserJobs(userId: Int): Pair<Boolean, Flow<List<Job>>>
}
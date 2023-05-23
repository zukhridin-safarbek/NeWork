package kg.zukhridin.nework.data.storage.repository

import kg.zukhridin.nework.data.entity.JobEntity
import kg.zukhridin.nework.data.storage.dao.JobDao
import kg.zukhridin.nework.domain.models.Job
import kg.zukhridin.nework.domain.storage.repositories.JobRepositoryStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JobRepositoryImplStorage @Inject constructor(
    private val jobDao: JobDao,
) : JobRepositoryStorage {

    override suspend fun insertJob(job: Job): Boolean {
        jobDao.insertJob(JobEntity.fromDto(job))
        return true
    }

    override suspend fun deleteJob(job: Job): Boolean {
        jobDao.deleteJobById(job.id)
        return true
    }

    override suspend fun getUserJobs(userId: Int): Pair<Boolean, Flow<List<Job>>> {
        val response = jobDao.getJobsByUserId(userId)
        return Pair(true, response.map { list -> list.map(JobEntity::toDto) })
    }

    override suspend fun clearJobs(): Int {
        return jobDao.clearJobs()
    }

    override suspend fun insertJobs(jobs: List<Job>) {
        jobDao.insertJobs(jobs.map { JobEntity.fromDto(it) })
    }
}
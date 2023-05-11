package kg.zukhridin.nework.repository.impl

import androidx.lifecycle.LiveData
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.database.dao.JobDao
import kg.zukhridin.nework.dto.Job
import kg.zukhridin.nework.entity.JobEntity
import kg.zukhridin.nework.entity.PostEntity
import kg.zukhridin.nework.repository.JobRepository
import kg.zukhridin.nework.service.APIService
import kg.zukhridin.nework.usecase.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val service: APIService,
    private val jobDao: JobDao,
    private val appAuth: AppAuth
) : JobRepository {
    override suspend fun getMyJobs(): Pair<Boolean, Flow<List<Job>>> {
        val response = GetMyJobsFromServerUseCase(service).getMyJobsFromServerUseCase()
        if (response.first) {
            response.second.value?.let { jobDao.insertJobs(it.map { job -> job.copy(userId = appAuth.authStateFlow.value?.id) }) }
        }
        val myJobs = GetMyJobsFromExternalStorageUseCase(jobDao).getMyJobsFromExternalStorage(
            appAuth.authStateFlow.value?.id ?: 0
        ).map {
            it.map(JobEntity::toDto)
        }
        return Pair(response.first, myJobs)
    }

    override suspend fun createJob(job: Job): Boolean {
        val res = InsertJobToServerUseCase(service).insertJobToServerUseCase(job)
        if (res.first) {
            if (res.second != null) {
                jobDao.insertJob(
                    JobEntity.fromDto(res.second!!).copy(userId = appAuth.authStateFlow.value?.id)
                )
            }
        }
        return res.first
    }

    override suspend fun deleteJob(job: Job): Boolean {
        DeleteMyJobFromExternalStorageUseCase(jobDao).deleteMyJobFromExternalStorage(job)
        DeleteMyJobByIdUseCase(service).deleteMyJobById(jobId = job.id)
        return true
    }

    override suspend fun getUserJobs(userId: Int): Pair<Boolean, Flow<List<Job>>> {
        val response = GetUsersJobUseCase(service).getUsersJobUseCase(userId)
        response.second.value?.let { jobDao.insertJobs(it.map { job -> job.copy(userId = userId) }) }
        val jobs = GetUserJobsFromExternalStorageUseCase(jobDao).getUserJobsFromExternalStorage(
            userId
        ).map {
            it.map(JobEntity::toDto)
        }
        return Pair(response.first, jobs)
    }
}
package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.database.dao.JobDao
import kg.zukhridin.nework.entity.JobEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

class GetUserJobsFromExternalStorageUseCase(private val jobDao: JobDao) {
    fun getUserJobsFromExternalStorage(userId: Int): Flow<List<JobEntity>>{
        return jobDao.getJobsByUserId(userId)
    }
}
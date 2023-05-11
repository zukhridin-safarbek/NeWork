package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.database.dao.JobDao
import kg.zukhridin.nework.entity.JobEntity
import kotlinx.coroutines.flow.Flow

class GetMyJobsFromExternalStorageUseCase(private val jobDao: JobDao) {
    suspend fun getMyJobsFromExternalStorage(userId: Int): Flow<List<JobEntity>> {
        return jobDao.getJobsByUserId(userId)
    }
}
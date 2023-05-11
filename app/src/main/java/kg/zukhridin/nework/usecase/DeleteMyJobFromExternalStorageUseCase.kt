package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.database.dao.JobDao
import kg.zukhridin.nework.dto.Job

class DeleteMyJobFromExternalStorageUseCase(private val jobDao: JobDao) {
    suspend fun deleteMyJobFromExternalStorage(job: Job) {
        jobDao.deleteJobById(job.id)
    }
}
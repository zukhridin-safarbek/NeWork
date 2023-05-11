package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.dto.Job
import kg.zukhridin.nework.service.APIService

class InsertJobToServerUseCase(private val service: APIService) {
    suspend fun insertJobToServerUseCase(job: Job): Pair<Boolean, Job?>{
        val response = service.insertJob(job)
        return Pair(response.isSuccessful, response.body())
    }
}
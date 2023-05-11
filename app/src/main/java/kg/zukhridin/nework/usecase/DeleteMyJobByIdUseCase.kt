package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.service.APIService

class DeleteMyJobByIdUseCase(private val service: APIService) {
    suspend fun deleteMyJobById(jobId: Int) {
        val response = service.deleteJob(jobId)
        println("response.isSuccessful: ${response.isSuccessful}")

    }
}
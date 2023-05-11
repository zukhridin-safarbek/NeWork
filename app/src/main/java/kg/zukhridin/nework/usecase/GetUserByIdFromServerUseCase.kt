package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.service.APIService

class GetUserByIdFromServerUseCase(private val service: APIService) {
    suspend fun getUserByIdFromServerUseCase(userId: Int): User? {
        val response = service.getUserById(userId)
        if (!response.isSuccessful) {
            throw Exception(response.message())
        }
        return response.body()
    }
}
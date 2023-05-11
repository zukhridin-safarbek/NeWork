package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.service.APIService

class DeletePostByIdFromServerUseCase(private val service:APIService) {
    suspend fun deletePostByIdFromServer(postId: Int){
        val response = service.deletePostById(postId)
        if (!response.isSuccessful) {
            throw Exception(response.message())
        }
    }
}
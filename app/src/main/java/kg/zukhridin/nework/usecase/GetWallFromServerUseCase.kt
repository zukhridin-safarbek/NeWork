package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.service.APIService

class GetWallFromServerUseCase(private val service: APIService) {
    suspend fun getWallFromServerUseCase(userId: Int): Map<Int, List<Post>> {
        val map = mutableMapOf<Int, List<Post>>()
        val wallsResponse = service.getWalls(userId)
        if (!wallsResponse.isSuccessful) {
            throw Exception(wallsResponse.message())
        }
        map[userId] = wallsResponse.body().orEmpty()
        return map
    }
}
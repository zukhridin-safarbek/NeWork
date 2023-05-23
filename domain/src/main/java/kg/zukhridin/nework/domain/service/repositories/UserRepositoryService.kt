package kg.zukhridin.nework.domain.service.repositories

import kg.zukhridin.nework.domain.models.PhotoModel
import kg.zukhridin.nework.domain.models.User
import kg.zukhridin.nework.domain.models.ErrorResponseModel

interface UserRepositoryService {
    suspend fun userAuthentication(login: String, password: String): Pair<Boolean, ErrorResponseModel?>
    suspend fun getUsers(): List<User>
    suspend fun getUserById(userId: Int): User?

    suspend fun registrationWithPhoto(
        login: String,
        password: String,
        name: String,
        photo: PhotoModel,
    ): Pair<Boolean, ErrorResponseModel?>

    suspend fun registrationWithOutPhoto(
        login: String,
        password: String,
        name: String,
    ): Pair<Boolean, ErrorResponseModel?>
}
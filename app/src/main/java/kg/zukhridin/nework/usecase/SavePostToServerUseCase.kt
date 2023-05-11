package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.dto.Attachment
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.service.APIService

class SavePostToServerUseCase(private val service: APIService) {
    suspend fun savePostToServer(post: Post): Boolean {
        if (post.attachment != null) {
            val media = SaveMediaToServerUseCase(service).saveMediaToServer(post.attachment.url)
            val response =
                service.insertPost(
                    post.copy(
                        attachment = Attachment(
                            media!!,
                            post.attachment.type
                        )
                    )
                )
            return if (!response.isSuccessful) {
                ApiResult.Error<Unit>(response.code().toString(), response.message())
                return false
            } else {
                ApiResult.Success(response.code().toString(), response.body() ?: Unit)
                true
            }
        } else {
            val response =
                service.insertPost(
                    post
                )
            return if (!response.isSuccessful) {
                ApiResult.Error<Unit>(response.code().toString(), response.message())
                return false
            } else {
                ApiResult.Success(response.code().toString(), response.body() ?: Unit)
                true
            }
        }
    }
}
package kg.zukhridin.nework.data.service.repository

import kg.zukhridin.nework.data.service.requests.PostAPIService
import kg.zukhridin.nework.data.util.getErrorBody
import kg.zukhridin.nework.domain.models.ErrorResponseModel
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.domain.service.repositories.PostRepositoryService
import javax.inject.Inject

class PostRepositoryImplService @Inject constructor(
    private val postService: PostAPIService
) : PostRepositoryService {

    override suspend fun insertPost(post: Post): Pair<Boolean, ErrorResponseModel> {
        if (post.attachment != null) {
            val response =
                postService.insertPost(
                    post
                )
            return if (response.isSuccessful) {
                Pair(response.isSuccessful, ErrorResponseModel(null))
            } else {
                Pair(response.isSuccessful, getErrorBody(response.errorBody()))
            }
        } else {
            val response =
                postService.insertPost(
                    post
                )
            return if (response.isSuccessful) {
                Pair(response.isSuccessful, ErrorResponseModel(null))
            } else {
                Pair(response.isSuccessful, getErrorBody(response.errorBody()))
            }
        }
    }

    override suspend fun deletePostById(id: Int) {
        postService.deletePostById(id)
    }


    override suspend fun updatePostById(post: Post) {
        if (post.id != 0) {
            postService.updatePostById(post)
        }
    }

    override suspend fun likeById(post: Post) {
        postService.likeById(post.id)
    }

    override suspend fun dislikeById(post: Post) {
        postService.dislikeById(post.id)
    }
}
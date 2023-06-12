package kg.zukhridin.nework.data.service.repository

import android.util.Log
import kg.zukhridin.nework.data.service.requests.APIService
import kg.zukhridin.nework.data.service.requests.PostAPIService
import kg.zukhridin.nework.data.service.requests.WallAPIService
import kg.zukhridin.nework.data.storage.dao.PostDao
import kg.zukhridin.nework.data.util.Constants.MY_LOG
import kg.zukhridin.nework.data.util.InsertMedia
import kg.zukhridin.nework.data.util.getErrorBody
import kg.zukhridin.nework.domain.models.Attachment
import kg.zukhridin.nework.domain.models.ErrorResponseModel
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.domain.service.repositories.PostRepositoryService
import kg.zukhridin.nework.domain.storage.repositories.PostRepositoryStorage
import javax.inject.Inject

class PostRepositoryImplService @Inject constructor(
    private val postService: PostAPIService,
    private val wallService: WallAPIService,
    private val mediaService: APIService,
    private val repositoryStorage: PostRepositoryStorage,
    private val postDao: PostDao
) : PostRepositoryService {

    override suspend fun insertPost(post: Post): Pair<Boolean, ErrorResponseModel> {
        if (post.attachment != null) {
            val media = InsertMedia(mediaService).insert(post.attachment?.url!!)
            val copiedPost = post.copy(
                attachment = Attachment(
                    media!!,
                    post.attachment!!.type
                )
            )
        repositoryStorage.insertPost(copiedPost)
            val response =
                postService.insertPost(
                    copiedPost
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